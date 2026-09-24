package com.dreamportal.tests;

import com.dreamportal.utils.DriverFactory;
import com.dreamportal.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Base class for all tests — handles driver setup/teardown
 * and screenshots on failure.
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtil.capture(driver, result.getName());
            if (screenshotPath != null) {
                try {
                    Allure.addAttachment("Screenshot on failure",
                            "image/png",
                            new FileInputStream(screenshotPath),
                            ".png");
                } catch (FileNotFoundException e) {
                    System.err.println("Could not attach screenshot to Allure: " + e.getMessage());
                }
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }
}
