package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.controller.CrawlUtil;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.WebDriverFactory;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NaverNewsCrawlService {

    private final NewsRepository newsRepository;
    private final WebDriverFactory webDriverFactory;

    @Scheduled(cron = "0 */5 * * * *")
    @Async
    public void crawlNaverEconomyNews() {
        int savedCount = 0;

        WebDriver driver = webDriverFactory.createHeadlessDriver();
        driver.get("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101"); // 경제 섹션

        try {
            CrawlUtil.sleep(3000);

            //  기사 링크만 따로 수집
            List<WebElement> articleElements = driver.findElements(By.cssSelector("a.sa_text_title"));
            List<String> links = new ArrayList<>();
            for (WebElement el : articleElements) {
                String href = CrawlUtil.safeGetAttr(driver, "a.sa_text_title", "href");
                if (href != null && !href.isEmpty()) {
                    links.add(href);
                }
            }

            //  각 링크마다 크롤링 진행
            for (int i = 0; i < Math.min(links.size(), 20); i++) {
                String link = links.get(i);
                driver.get(link);
                CrawlUtil.sleep(2000);

                String title = CrawlUtil.safeGetText(driver, "h2#title_area");

                String source = null;
                try {
                    source = CrawlUtil.safeGetAttr(driver, "div.media_end_head_top img", "alt");

                } catch (Exception ignored) {}

                String dateStr = null;
                try {
                    dateStr = CrawlUtil.safeGetAttr(driver, "span.media_end_head_info_datestamp_time._ARTICLE_DATE_TIME", "data-date-time");
                } catch (Exception ignored) {}

                LocalDateTime date = null;
                if (dateStr != null && !dateStr.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    date = LocalDateTime.parse(dateStr, formatter);
                }

                String content = "";
                try {
                    content = CrawlUtil.safeGetText(driver, "article#dic_area");
                } catch (Exception ignored) {}

                String imageUrl = null;
                try {
                    List<WebElement> images = driver.findElements(By.cssSelector("article#dic_area img"));
                    for (WebElement img : images) {
                        String src = CrawlUtil.safeGetAttr(driver, "article#dic_area img", "src");
                        if (src != null && !src.isEmpty()) {
                            imageUrl = src;
                            break;
                        }
                    }
                } catch (Exception ignored) {}
                if (imageUrl == null) {
                    imageUrl = "";
                }

                boolean exists = newsRepository.findByUrl(link).isPresent()
                        || newsRepository.findByTitle(title).isPresent();
                if (exists) {
                    System.out.println("[NAVER] 이미 저장된 기사: " + title);
                    continue;
                }

                News news = News.builder()
                        .title(title)
                        .source(source)
                        .date(date)
                        .content(content)
                        .url(link)
                        .imageUrl(imageUrl)
                        .thema(Thema.ETC)
                        .view(0L)
                        .build();

                newsRepository.save(news);
                savedCount++;
                System.out.println("[NAVER] 저장 완료 (" + savedCount + "): " + title);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

}
