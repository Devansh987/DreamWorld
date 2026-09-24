package com.dreamportal.tests;

import com.dreamportal.pages.DreamsDiaryPage;
import com.dreamportal.pages.DreamsDiaryPage.DreamEntry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import okhttp3.*;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Feature("AI Dream Classification")
public class AIDreamClassificationTest extends BaseTest {

    private String apiKey;

    @BeforeClass
    public void checkApiKey() {
        apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new SkipException("OPENAI_API_KEY not set — skipping AI tests");
        }
    }

    @Test
    @Description("Use OpenAI API to classify each dream and compare with the website's classification")
    public void testAIClassificationMatchesWebsite() {
        DreamsDiaryPage diaryPage = new DreamsDiaryPage(driver);
        diaryPage.open();

        List<DreamEntry> dreams = diaryPage.getAllDreams();

        for (DreamEntry dream : dreams) {
            String aiClassification = classifyDream(dream.name());
            Assert.assertEquals(aiClassification, dream.type(),
                    "AI classified '" + dream.name() + "' as '" + aiClassification
                            + "' but website says '" + dream.type() + "'");
        }
    }

    /**
     * Sends the dream name to OpenAI and asks for a strict "Good" or "Bad" classification.
     */
    private String classifyDream(String dreamName) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        String prompt = "Classify the following dream as exactly \"Good\" or \"Bad\". "
                + "Respond with only one word: Good or Bad. Nothing else.\n\n"
                + "Dream: " + dreamName;

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4o-mini");
        requestBody.add("messages", new com.google.gson.JsonArray());
        requestBody.getAsJsonArray("messages").add(message);
        requestBody.addProperty("max_tokens", 5);
        requestBody.addProperty("temperature", 0);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(),
                        MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Assert.fail("OpenAI API returned " + response.code() + " for dream: " + dreamName);
            }

            String body = response.body().string();
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            String content = json.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString()
                    .trim();

            // Normalize response
            if (content.equalsIgnoreCase("good")) return "Good";
            if (content.equalsIgnoreCase("bad")) return "Bad";

            Assert.fail("AI returned unexpected response for '" + dreamName + "': " + content);
            return content;

        } catch (IOException e) {
            Assert.fail("API call failed for '" + dreamName + "': " + e.getMessage());
            return null;
        }
    }
}
