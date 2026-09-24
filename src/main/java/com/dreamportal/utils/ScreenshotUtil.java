package com.dreamportal.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "screenshots";

    public static String capture(WebDriver driver, String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);

            String filename = testName + "_" + System.currentTimeMillis() + ".png";
            Path dest = dir.resolve(filename);
            Files.copy(src.toPath(), dest);

            return dest.toString();
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
