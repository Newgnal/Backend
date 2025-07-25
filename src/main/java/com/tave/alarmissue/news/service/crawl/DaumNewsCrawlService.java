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
import com.tave.alarmissue.notification.service.KeywordNewsNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
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
public class DaumNewsCrawlService {

    private final NewsRepository newsRepository;
    private final WebDriverFactory webDriverFactory;
    private final S3Uploader s3Uploader;
    private final AiService aiService;
    private final KeywordNewsNotificationService keywordNewsNotificationService;

    @Scheduled(cron = "0 */30 * * * *")
    @Async
    public void crawlDaumEconomyNews() {

        WebDriver driver = webDriverFactory.createHeadlessDriver();
        driver.get("https://news.daum.net/economy");

        try {
            List<WebElement> items = driver.findElements(By.cssSelector("ul.list_newsheadline2 > li"));
            List<String> links = new ArrayList<>();

            // 링크만 따로 저장
            for (WebElement item : items) {
                try {
                    WebElement linkElement = item.findElement(By.cssSelector("a.item_newsheadline2"));
                    String url = linkElement.getAttribute("href");
                    if (url != null && !url.isEmpty()) {
                        links.add(url);
                    }
                } catch (Exception e) {
                    log.error("링크 추출 에러: {}", e.getMessage());
                }
            }

            List<String> titles = new ArrayList<>();
            List<String> validLinks = new ArrayList<>();

            // 링크별 제목 수집
            for (int i = 0; i < Math.min(links.size(), 20); i++) {
                String url = links.get(i);
                driver.get(url);
                CrawlUtil.sleep(1500);

                String title = CrawlUtil.safeGetText(driver, "h3.tit_view");
                if (title != null && !title.isEmpty()) {
                    titles.add(title);
                    validLinks.add(url);
                }
            }

            // DB에 이미 저장된 타이틀들 가져오기
            List<String> existingTitles = newsRepository.findAllTitlesByTitleIn(titles);

            Map<News, String> newsContentMap = new HashMap<>();

            // 중복 아닌 뉴스만 크롤링 후 저장 준비
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i);

                if (existingTitles.contains(title)) {
                    log.info("[DAUM] 이미 저장된 기사 제목: {}", title);
                    continue;
                }

                String url = validLinks.get(i);
                driver.get(url);
                CrawlUtil.sleep(1500);

                String source = CrawlUtil.safeGetText(driver, "a#kakaoServiceLogo");
                String dateStr = CrawlUtil.safeGetText(driver, "span.num_date");

                LocalDateTime date = null;
                if (dateStr != null && !dateStr.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. HH:mm");
                    date = LocalDateTime.parse(dateStr, formatter);
                }

                List<WebElement> paras = driver.findElements(By.cssSelector("p[dmcf-ptype='general']"));
                StringBuilder contentBuilder = new StringBuilder();
                for (WebElement p : paras) {
                    String txt = p.getText();
                    if (!txt.isBlank()) contentBuilder.append(txt).append("\n");
                }

                String rawContent = contentBuilder.toString();
                String fileName = "news/daum/" + UUID.randomUUID() + ".txt";
                String contentUrl = s3Uploader.uploadContent(contentBuilder.toString(), fileName);

                String imageUrl = "";
                try {
                    imageUrl = CrawlUtil.safeGetAttr(driver, "img.thumb_g_article", "data-org-src");
                } catch (Exception e) {
                    // 빈 문자열 유지
                }

                String imageCaption = "";
                try {
                    imageCaption = CrawlUtil.safeGetText(driver, "figcaption.txt_caption.default_figure");
                } catch (Exception e) {
                }

                News news = News.builder()
                        .title(title)
                        .source(source)
                        .date(date)
                        .contentUrl(contentUrl)
                        .url(url)
                        .imageUrl(imageUrl)
                        .imageCaption(imageCaption)
                        .thema(Thema.ETC) // 기본값 ETC
                        .view(0L)
                        .commentNum(0L)
                        .voteNum(0L)
                        .summary(null)
                        .build();

                newsContentMap.put(news, rawContent);
            }

            List<News> savedNewsList = newsRepository.saveAll(newsContentMap.keySet());
            List<News> updatedNewsList = new ArrayList<>();
            log.info("[DAUM] 저장 완료 ({}건)", savedNewsList.size());

            processKeywordNotifications(savedNewsList);

            for (News savedNews : savedNewsList) {
                String rawContent = newsContentMap.get(savedNews);
                String escapedContent = rawContent.replace("\n", "\\n");

                Thema themaEnum = Thema.ETC;  // 기본값 ETC
                try {
                    ThemaResponse themaResponse = aiService.analyzeThema(escapedContent).block();
                    if (themaResponse == null) {
                        log.warn("Thema 분석 결과가 null입니다.");
                    } else {
                        themaEnum = Thema.fromString(themaResponse.theme());
                        log.info("분석 결과: {}", themaEnum);
                    }
                } catch (Exception e) {
                    log.error("Thema 분석 중 에러 발생: {}", e.getMessage(), e);
                }

                String summary = null;
                try {
                    SummaryResponse summaryResponse = aiService.analyzeSummary(escapedContent).block();
                    if (summaryResponse == null) {
                        log.warn("Summary 분석 결과가 null입니다.");
                    } else {
                        summary = summaryResponse.summary();
                        log.info("요약 분석 결과: {}", summary);
                    }
                } catch (Exception e) {
                    log.error("Summary 분석 중 에러 발생: {}", e.getMessage(), e);
                }

                try {
                    // 업데이트된 뉴스 저장
                    News updatedNews = savedNews.toBuilder()
                            .thema(themaEnum)
                            .summary(summary)
                            .build();
                    newsRepository.save(updatedNews);
                    updatedNewsList.add(updatedNews);
                } catch (Exception e) {
                    log.error("뉴스 저장 중 에러 발생: {}", e.getMessage(), e);
                }
            }

            updateSentimentForNews(updatedNewsList);

        } catch (Exception e) {
            log.error("뉴스 크롤링 중 에러 발생", e);
        } finally {
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

    private void processKeywordNotifications(List<News> newsList) {
        if (newsList.isEmpty()) {
            return;
        }

        log.info("[DAUM] 키워드 알림 처리 시작 - 대상 뉴스 개수: {}", newsList.size());

        for (News news : newsList) {
            try {
                keywordNewsNotificationService.sendKeywordNewsNotifications(news);
            } catch (Exception e) {
                log.error("[DAUM] 뉴스 알림 처리 중 오류 발생 - 뉴스 ID: {}, 제목: {}",
                        news.getId(), news.getTitle(), e);
            }
        }
    }
}

