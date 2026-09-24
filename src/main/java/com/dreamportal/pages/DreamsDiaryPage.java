package com.dreamportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DreamsDiaryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String URL = "https://arjitnigam.github.io/myDreams/dreams-diary.html";

    // Locators
    private final By diaryTable = By.id("dreamsDiary");
    private final By tableRows = By.cssSelector("#dreamsDiary tbody tr");

    public DreamsDiaryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(diaryTable));
    }

    public int getDreamCount() {
        return driver.findElements(tableRows).size();
    }

    /**
     * Returns all dream entries from the table.
     * Each entry is a simple record with name, daysAgo, and type.
     */
    public List<DreamEntry> getAllDreams() {
        List<DreamEntry> dreams = new ArrayList<>();
        List<WebElement> rows = driver.findElements(tableRows);

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            String name = cells.get(0).getText().trim();
            String daysAgo = cells.get(1).getText().trim();
            String type = cells.get(2).getText().trim();
            dreams.add(new DreamEntry(name, daysAgo, type));
        }

        return dreams;
    }

    /**
     * Simple data class for a dream entry row.
     */
    public record DreamEntry(String name, String daysAgo, String type) {
    }
}
