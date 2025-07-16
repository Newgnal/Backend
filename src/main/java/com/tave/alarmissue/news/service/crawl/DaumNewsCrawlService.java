package com.tave.alarmissue.news.service.crawl;

import com.tave.alarmissue.news.controller.CrawlUtil;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.WebDriverFactory;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DaumNewsCrawlService {

    private final NewsRepository newsRepository;
    private final WebDriverFactory webDriverFactory;
    private final S3Uploader s3Uploader;

    @Scheduled(cron = "0 */30 * * * *")
    @Async
    public void crawlDaumEconomyNews() {
        int savedCount = 0;

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

            List<News> newsToSave = new ArrayList<>();

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

                String content = contentBuilder.toString();
                log.info("[DAUM] 크롤링된 본문 내용: {}", content);

                String fileName = "news/daum/" + UUID.randomUUID() + ".txt";
                String contentUrl = s3Uploader.uploadContent(contentBuilder.toString(), fileName);

                String imageUrl = "";
                try {
                    imageUrl = CrawlUtil.safeGetAttr(driver, "img.thumb_g_article", "data-org-src");
                } catch (Exception e) {
                    // 빈 문자열 유지
                }

                News news = News.builder()
                        .title(title)
                        .source(source)
                        .date(date)
                        .contentUrl(contentUrl)
                        .url(url)
                        .imageUrl(imageUrl)
                        .thema(Thema.ETC) // 기본값 ETC
                        .view(0L)
                        .commentNum(0L)
                        .voteNum(0L)
                        .summary(null)
                        .build();

                newsToSave.add(news);
            }

            newsRepository.saveAll(newsToSave);
            savedCount += newsToSave.size();
            log.info("[DAUM] 저장 완료 ({})", savedCount);

        } catch (Exception e) {
            log.error("뉴스 크롤링 중 에러 발생", e);
        } finally {
            driver.quit();
        }
    }

}
