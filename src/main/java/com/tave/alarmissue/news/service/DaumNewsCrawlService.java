package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.controller.CrawlUtil;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.WebDriverFactory;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
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
public class DaumNewsCrawlService {

    private final NewsRepository newsRepository;
    private final WebDriverFactory webDriverFactory;

    @Scheduled(cron = "0 */5 * * * *")
    @Async
    public void crawlDaumEconomyNews() {
        int savedCount= 0;

        WebDriver driver = webDriverFactory.createHeadlessDriver();
        driver.get("https://news.daum.net/economy");

        try {
            List<WebElement> items = driver.findElements(By.cssSelector("ul.list_newsheadline2 > li"));
            List<String> links = new ArrayList<>();

            // 링크만 따로 저장
            for (WebElement item : items) {
                try {
                    WebElement linkElement = item.findElement(By.cssSelector("a.item_newsheadline2"));
                    String url = CrawlUtil.safeGetAttr(driver, "a.item_newsheadline2", "href");
                    if (url != null && !url.isEmpty()) {
                        links.add(url);
                    }
                } catch (Exception e) {
                    System.out.println("링크 추출 에러: " + e.getMessage());
                }
            }

            for (int i = 0; i < Math.min(links.size(), 20); i++) {
                String url = links.get(i);
                driver.get(url);
                CrawlUtil.sleep(1500);

                String title = CrawlUtil.safeGetText(driver, "h3.tit_view");
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

                String imageUrl = null;
                try {
                    imageUrl = CrawlUtil.safeGetAttr(driver, "img.thumb_g_article", "data-org-src");
                } catch (Exception e) {
                    imageUrl = "";  // 빈 문자열로 처리
                }

                // 중복 체크
                if (newsRepository.findByUrl(url).isPresent() || newsRepository.findByTitle(title).isPresent()) {
                    System.out.println("이미 저장된 기사: " + title);
                    continue;
                }

                News news = News.builder()
                        .title(title)
                        .source(source)
                        .date(date)
                        .content(contentBuilder.toString())
                        .url(url)
                        .imageUrl(imageUrl)
                        .thema(Thema.ETC) // 일단 기본값 ETC
                        .view(0L)
                        .build();

                newsRepository.save(news);
                savedCount++;
                System.out.println("DAUM 저장 완료 (" + savedCount + "): " + title);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

}
