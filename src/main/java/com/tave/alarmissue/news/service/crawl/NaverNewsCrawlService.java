package com.tave.alarmissue.news.service.crawl;

import com.tave.alarmissue.ai.dto.response.SentimentResponse;
import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import com.tave.alarmissue.ai.service.AiService;
import com.tave.alarmissue.news.controller.CrawlUtil;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.WebDriverFactory;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverNewsCrawlService {

    private final NewsRepository newsRepository;
    private final WebDriverFactory webDriverFactory;
    private final S3Uploader s3Uploader;
    private final AiService aiService;

    @Scheduled(cron = "0 */30 * * * *")
    @Async
    public void crawlNaverEconomyNews() {
        WebDriver driver = webDriverFactory.createHeadlessDriver();
        try {
            driver.get("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101"); // 경제 섹션
            CrawlUtil.sleep(3000);

            // 기사 링크만 수집
            List<WebElement> articleElements = driver.findElements(By.cssSelector("a.sa_text_title"));
            List<String> links = new ArrayList<>();
            for (WebElement el : articleElements) {
                String href = el.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    links.add(href);
                }
            }

            // 타이틀과 유효 링크 수집
            List<String> titles = new ArrayList<>();
            List<String> validLinks = new ArrayList<>();
            for (int i = 0; i < Math.min(links.size(), 20); i++) {
                String link = links.get(i);
                driver.get(link);
                CrawlUtil.sleep(2000);

                String title = CrawlUtil.safeGetText(driver, "h2#title_area");
                if (title != null && !title.isEmpty()) {
                    titles.add(title);
                    validLinks.add(link);
                }
            }

            // 기존 저장된 타이틀 조회
            List<String> existingTitles = newsRepository.findAllTitlesByTitleIn(titles);

            Map<News, String> newsContentMap = new HashMap<>();

            // 중복 아닌 뉴스만 상세 크롤링 후 저장 준비
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i);

                if (existingTitles.contains(title)) {
                    log.info("[NAVER] 이미 저장된 기사 제목: {}", title);
                    continue;
                }

                String link = validLinks.get(i);
                driver.get(link);
                CrawlUtil.sleep(2000);

                String source = null;
                try {
                    source = CrawlUtil.safeGetAttr(driver, "div.media_end_head_top img", "alt");
                } catch (Exception ignored) {
                }

                String dateStr = null;
                try {
                    dateStr = CrawlUtil.safeGetAttr(driver, "span.media_end_head_info_datestamp_time._ARTICLE_DATE_TIME", "data-date-time");
                } catch (Exception ignored) {
                }

                LocalDateTime date = null;
                if (dateStr != null && !dateStr.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    date = LocalDateTime.parse(dateStr, formatter);
                }

                StringBuilder contentBuilder = new StringBuilder();
                String content = "";
                try {
                    content = CrawlUtil.safeGetText(driver, "article#dic_area");
                    contentBuilder.append(content);
                } catch (Exception ignored) {
                }

                String rawContent = contentBuilder.toString();
                String fileName = "news/naver/" + UUID.randomUUID() + ".txt";
                String contentUrl = s3Uploader.uploadContent(content, fileName);
                log.info("Uploaded content URL: {}", contentUrl);

                String imageUrl = "";
                try {
                    List<WebElement> images = driver.findElements(By.cssSelector("article#dic_area img"));
                    for (WebElement img : images) {
                        String src = CrawlUtil.safeGetAttr(driver, "article#dic_area img", "src");
                        if (src != null && !src.isEmpty()) {
                            imageUrl = src;
                            break;
                        }
                    }
                } catch (Exception ignored) {
                }

                String imageCaption = "";
                try {
                    imageCaption = CrawlUtil.safeGetText(driver, "em.img_desc");
                } catch (Exception ignored) {
                }

                boolean exists = newsRepository.findByUrl(link).isPresent()
                        || newsRepository.findByTitle(title).isPresent();
                if (exists) {
                    log.info("[NAVER] 이미 저장된 기사: {}", title);
                    continue;
                }

                News news = News.builder()
                        .title(title)
                        .source(source)
                        .date(date)
                        .contentUrl(contentUrl)
                        .url(link)
                        .imageUrl(imageUrl)
                        .imageCaption(imageCaption)
                        .thema(Thema.ETC)
                        .view(0L)
                        .commentNum(0L)
                        .voteNum(0L)
                        .summary(null)
                        .build();

                newsContentMap.put(news, rawContent);
            }

            // 저장
            List<News> savedNewsList = newsRepository.saveAll(newsContentMap.keySet());
            List<News> updatedNewsList = new ArrayList<>();
            log.info("[NAVER] 저장 완료 ({}건)", savedNewsList.size());


            for (News savedNews : savedNewsList) {
                String rawContent = newsContentMap.get(savedNews);
                String escapedContent = rawContent.replace("\n", "\\n");

                Thema themaEnum = Thema.ETC;

                try {
                    ThemaResponse themaResponse = aiService.analyzeThema(escapedContent).block();
                    themaEnum = Thema.fromString(themaResponse.theme());
                    log.info("분석 결과 (Thema): {}", themaEnum);
                } catch (Exception e) {
                    log.error("Thema 분석 실패: {}", e.getMessage(), e);
                }


                String summary = null;

                try {
                    SummaryResponse summaryResponse = aiService.analyzeSummary(escapedContent).block();
                    summary = summaryResponse.summary();
                    log.info("요약 분석 결과: {}", summary);
                } catch (Exception e) {
                    log.error("Summary 분석 실패: {}", e.getMessage(), e);
                }

                News updatedNews = savedNews.toBuilder()
                        .thema(themaEnum)
                        .summary(summary)
                        .build();

                newsRepository.save(updatedNews);
                updatedNewsList.add(updatedNews);
            }

            updateSentimentForNews(updatedNewsList);

        } catch (Exception e) {
            log.error("[NAVER] 뉴스 크롤링 중 에러 발생", e);
            driver.quit();
        }
    }

    public void updateSentimentForNews(List<News> newsList) {
        for (News news : newsList) {
            String title = news.getTitle();
            String escapedTitle = title.replace("\n", "\\n");
            Float sentimentScore = 0.0f;

            try {
                SentimentResponse sentimentResponse = aiService.analyzeSentiment(List.of(escapedTitle)).block();
                if (sentimentResponse != null) {
                    sentimentScore = sentimentResponse.getScore();
                    log.info("감정 분석 결과: {}", sentimentScore);
                }
            } catch (Exception e) {
                log.error("Sentiment 분석 중 에러 발생: {}", e.getMessage(), e);
            }

            try {
                News updatedNews = news.toBuilder()
                        .sentiment(sentimentScore.doubleValue())
                        .build();
                newsRepository.save(updatedNews);
            } catch (Exception e) {
                log.error("뉴스 저장 중 에러 발생: {}", e.getMessage(), e);
            }
        }

    }
}