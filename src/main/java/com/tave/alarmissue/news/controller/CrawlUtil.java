package com.tave.alarmissue.news.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CrawlUtil {
    public static String safeGetText(WebDriver driver, String selector) {
        try {
            return driver.findElement(By.cssSelector(selector)).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public static String safeGetAttr(WebDriver driver, String selector, String attr) {
        try {
            return driver.findElement(By.cssSelector(selector)).getAttribute(attr);
        } catch (Exception e) {
            return null;
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
