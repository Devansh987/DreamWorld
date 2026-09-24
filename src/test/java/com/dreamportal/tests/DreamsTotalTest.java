package com.dreamportal.tests;

import com.dreamportal.pages.DreamsDiaryPage;
import com.dreamportal.pages.DreamsDiaryPage.DreamEntry;
import com.dreamportal.pages.DreamsTotalPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Feature("Dreams Summary")
public class DreamsTotalTest extends BaseTest {

    @Test
    @Description("Verify Good Dreams count is 6")
    public void testGoodDreamsCount() {
        DreamsTotalPage totalPage = new DreamsTotalPage(driver);
        totalPage.open();

        Assert.assertEquals(totalPage.getGoodDreams(), 6,
                "Good Dreams should be 6");
    }

    @Test
    @Description("Verify Bad Dreams count is 4")
    public void testBadDreamsCount() {
        DreamsTotalPage totalPage = new DreamsTotalPage(driver);
        totalPage.open();

        Assert.assertEquals(totalPage.getBadDreams(), 4,
                "Bad Dreams should be 4");
    }

    @Test
    @Description("Verify Total Dreams count is 10")
    public void testTotalDreamsCount() {
        DreamsTotalPage totalPage = new DreamsTotalPage(driver);
        totalPage.open();

        Assert.assertEquals(totalPage.getTotalDreams(), 10,
                "Total Dreams should be 10");
    }

    @Test
    @Description("Verify recurring dreams by analyzing diary data — 'Flying over mountains' and 'Lost in maze' should be recurring, and total recurring count should be 2")
    public void testRecurringDreamsFromDiaryData() {
        // Step 1: Read all dream names from the diary
        DreamsDiaryPage diaryPage = new DreamsDiaryPage(driver);
        diaryPage.open();

        List<DreamEntry> allDreams = diaryPage.getAllDreams();
        List<String> dreamNames = allDreams.stream()
                .map(DreamEntry::name)
                .collect(Collectors.toList());

        // Step 2: Count occurrences of each dream name
        Map<String, Integer> nameCount = new HashMap<>();
        for (String name : dreamNames) {
            nameCount.put(name, nameCount.getOrDefault(name, 0) + 1);
        }

        // Step 3: Identify recurring dreams (appear more than once)
        List<String> recurringDreams = nameCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        // Step 4: Verify specific recurring dreams
        Assert.assertTrue(recurringDreams.contains("Flying over mountains"),
                "'Flying over mountains' should be identified as a recurring dream");
        Assert.assertTrue(recurringDreams.contains("Lost in maze"),
                "'Lost in maze' should be identified as a recurring dream");

        // Step 5: Verify total recurring count matches
        Assert.assertEquals(recurringDreams.size(), 2,
                "Should have exactly 2 unique recurring dreams");

        // Step 6: Compare with summary page
        DreamsTotalPage totalPage = new DreamsTotalPage(driver);
        totalPage.open();

        int summaryRecurring = totalPage.getRecurringDreams();
        Assert.assertEquals(summaryRecurring, recurringDreams.size(),
                "Summary page recurring count should match calculated count from diary");
    }
}
