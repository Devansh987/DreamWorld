package com.dreamportal.tests;

import com.dreamportal.pages.DreamsDiaryPage;
import com.dreamportal.pages.DreamsDiaryPage.DreamEntry;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Feature("Dreams Diary")
public class DreamsDiaryTest extends BaseTest {

    @Test
    @Description("Verify exactly 10 dream entries exist in the diary")
    public void testExactly10DreamEntries() {
        DreamsDiaryPage diaryPage = new DreamsDiaryPage(driver);
        diaryPage.open();

        Assert.assertEquals(diaryPage.getDreamCount(), 10,
                "Diary should have exactly 10 dream entries");
    }

    @Test
    @Description("Verify every row has non-empty Dream Name, Days Ago, and Dream Type")
    public void testNoCellsAreEmpty() {
        DreamsDiaryPage diaryPage = new DreamsDiaryPage(driver);
        diaryPage.open();

        List<DreamEntry> dreams = diaryPage.getAllDreams();

        for (int i = 0; i < dreams.size(); i++) {
            DreamEntry dream = dreams.get(i);
            Assert.assertFalse(dream.name().isEmpty(),
                    "Row " + (i + 1) + ": Dream Name should not be empty");
            Assert.assertFalse(dream.daysAgo().isEmpty(),
                    "Row " + (i + 1) + ": Days Ago should not be empty");
            Assert.assertFalse(dream.type().isEmpty(),
                    "Row " + (i + 1) + ": Dream Type should not be empty");
        }
    }

    @Test
    @Description("Verify Dream Type is only 'Good' or 'Bad' for every entry")
    public void testDreamTypeIsGoodOrBad() {
        DreamsDiaryPage diaryPage = new DreamsDiaryPage(driver);
        diaryPage.open();

        List<DreamEntry> dreams = diaryPage.getAllDreams();

        for (int i = 0; i < dreams.size(); i++) {
            String type = dreams.get(i).type();
            Assert.assertTrue(type.equals("Good") || type.equals("Bad"),
                    "Row " + (i + 1) + ": Dream Type should be 'Good' or 'Bad', but was '" + type + "'");
        }
    }
}
