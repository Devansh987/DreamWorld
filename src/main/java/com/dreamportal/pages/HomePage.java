package com.dreamportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String URL = "https://arjitnigam.github.io/myDreams/";

    // Locators
    private final By loadingAnimation = By.id("loadingAnimation");
    private final By mainContent = By.id("mainContent");
    private final By dreamButton = By.id("dreamButton");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
    }

    public boolean isLoadingAnimationVisible() {
        return driver.findElement(loadingAnimation).isDisplayed();
    }

    public void waitForLoadingToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingAnimation));
    }

    public boolean isMainContentVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mainContent)).isDisplayed();
    }

    public boolean isDreamButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dreamButton)).isDisplayed();
    }

    public void clickMyDreams() {
        wait.until(ExpectedConditions.elementToBeClickable(dreamButton)).click();
    }

    /**
     * After clicking "My Dreams", two new tabs open.
     * Returns the set of all window handles.
     */
    public Set<String> getAllWindowHandles() {
        // Wait a moment for both tabs to open
        wait.until(driver -> driver.getWindowHandles().size() >= 3);
        return driver.getWindowHandles();
    }

    /**
     * Switches to the tab whose URL contains the given substring.
     */
    public void switchToTabContaining(String urlPart) {
        String original = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getCurrentUrl().contains(urlPart)) {
                return;
            }
        }
        // If not found, switch back
        driver.switchTo().window(original);
    }
}
