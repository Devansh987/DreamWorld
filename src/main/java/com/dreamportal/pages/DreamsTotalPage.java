package com.dreamportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DreamsTotalPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String URL = "https://arjitnigam.github.io/myDreams/dreams-total.html";

    // Locators
    private final By summaryTable = By.id("dreamsTotal");
    private final By tableRows = By.cssSelector("#dreamsTotal tbody tr");

    public DreamsTotalPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(summaryTable));
    }

    /**
     * Reads all summary rows into a map: "Good Dreams" -> 6, etc.
     */
    public Map<String, Integer> getSummaryData() {
        Map<String, Integer> data = new HashMap<>();
        List<WebElement> rows = driver.findElements(tableRows);

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            String label = cells.get(0).getText().trim();
            int value = Integer.parseInt(cells.get(1).getText().trim());
            data.put(label, value);
        }

        return data;
    }

    public int getGoodDreams() {
        return getSummaryData().get("Good Dreams");
    }

    public int getBadDreams() {
        return getSummaryData().get("Bad Dreams");
    }

    public int getTotalDreams() {
        return getSummaryData().get("Total Dreams");
    }

    public int getRecurringDreams() {
        return getSummaryData().get("Recurring Dreams");
    }
}
