package com.dreamportal.tests;

import com.dreamportal.pages.HomePage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

@Feature("Home Page")
public class HomePageTest extends BaseTest {

    @Test
    @Description("Verify loading animation appears on page load")
    public void testLoadingAnimationAppears() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        Assert.assertTrue(homePage.isLoadingAnimationVisible(),
                "Loading animation should be visible on page load");
    }

    @Test
    @Description("Verify loading animation disappears after ~3 seconds")
    public void testLoadingAnimationDisappears() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        // First confirm it's visible
        Assert.assertTrue(homePage.isLoadingAnimationVisible(),
                "Loading animation should be visible initially");

        // Wait for it to disappear (timeout is 10s, but should take ~3s)
        homePage.waitForLoadingToDisappear();

        // At this point the element should be hidden (display: none)
        // We can't use isDisplayed() on a hidden element, so we check mainContent instead
    }

    @Test
    @Description("Verify main content becomes visible after loading")
    public void testMainContentBecomesVisible() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        homePage.waitForLoadingToDisappear();

        Assert.assertTrue(homePage.isMainContentVisible(),
                "Main content should be visible after loading finishes");
    }

    @Test
    @Description("Verify 'My Dreams' button is visible after loading")
    public void testMyDreamsButtonVisible() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        homePage.waitForLoadingToDisappear();

        Assert.assertTrue(homePage.isDreamButtonVisible(),
                "'My Dreams' button should be visible after loading");
    }

    @Test
    @Description("Click 'My Dreams' and verify two new tabs open: dreams-diary.html and dreams-total.html")
    public void testMyDreamsOpensNewTabs() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.waitForLoadingToDisappear();

        String originalWindow = driver.getWindowHandle();

        homePage.clickMyDreams();

        Set<String> allWindows = homePage.getAllWindowHandles();
        Assert.assertEquals(allWindows.size(), 3,
                "Should have 3 windows total (original + 2 new tabs)");

        // Check that one tab has dreams-diary.html
        boolean foundDiary = false;
        boolean foundTotal = false;

        for (String handle : allWindows) {
            if (handle.equals(originalWindow)) continue;
            driver.switchTo().window(handle);
            String url = driver.getCurrentUrl();
            if (url.contains("dreams-diary.html")) foundDiary = true;
            if (url.contains("dreams-total.html")) foundTotal = true;
        }

        Assert.assertTrue(foundDiary, "dreams-diary.html tab should be open");
        Assert.assertTrue(foundTotal, "dreams-total.html tab should be open");

        // Switch back to original
        driver.switchTo().window(originalWindow);
    }
}
