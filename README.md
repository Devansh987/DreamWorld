# Dream Portal — QA Automation

Selenium WebDriver automation tests for the [Dream Portal](https://arjitnigam.github.io/myDreams/) website.

## Website Under Test

The Dream Portal is a simple dream tracking website with three pages:

- **Home Page** (`index.html`) — Loading animation followed by a "My Dreams" button
- **Dreams Diary** (`dreams-diary.html`) — Table of 10 dream entries with names, dates, and types
- **Dreams Summary** (`dreams-total.html`) — Aggregated counts of good/bad/total/recurring dreams

## Tech Stack

- Java 17
- Selenium WebDriver 4.x
- Maven
- TestNG
- Page Object Model (POM)
- Allure Reports
- WebDriverWait (explicit waits)

## Project Structure

```
src/
├── main/java/com/dreamportal/
│   ├── pages/
│   │   ├── HomePage.java          # Home page interactions
│   │   ├── DreamsDiaryPage.java   # Diary table data extraction
│   │   └── DreamsTotalPage.java   # Summary page data reading
│   └── utils/
│       ├── DriverFactory.java     # Chrome WebDriver setup
│       └── ScreenshotUtil.java    # Screenshot capture on failure
└── test/java/com/dreamportal/tests/
    ├── BaseTest.java              # Setup/teardown + screenshot attachment
    ├── HomePageTest.java          # Home page tests
    ├── DreamsDiaryTest.java       # Diary validation tests
    ├── DreamsTotalTest.java       # Summary + recurring dream tests
    └── AIDreamClassificationTest.java  # AI bonus test
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Chrome browser installed

## How to Install Dependencies

```bash
mvn clean install -DskipTests
```

## How to Run Tests

Run the core functional tests:

```bash
mvn test
```

Run all tests including AI classification (requires API key):

```bash
mvn test -Dsurefire.suiteXmlFiles=testng-all.xml
```

## How to Generate/View Allure Reports

After running tests, generate the report:

```bash
mvn allure:serve
```

This opens the report in your browser. To just generate without opening:

```bash
mvn allure:report
```

The report will be in `target/site/allure-maven-plugin/`.

## AI Bonus — OpenAI Dream Classification

The AI test sends each dream name to the OpenAI API and asks it to classify the dream as "Good" or "Bad", then compares the result with the website's classification.

### Setup

Set the API key as an environment variable:

```bash
# Windows
set OPENAI_API_KEY=your-key-here

# Linux/Mac
export OPENAI_API_KEY=your-key-here
```

If `OPENAI_API_KEY` is not set, the AI test is skipped automatically — it does not affect core tests.

**Never commit API keys to the repository.**

## What the Tests Cover

### Home Page
- Loading animation appears on page load
- Loading animation disappears after ~3 seconds
- Main content becomes visible
- "My Dreams" button is visible
- Clicking "My Dreams" opens two new tabs (diary and summary)

### Dreams Diary
- Exactly 10 dream entries exist
- No cells are empty (name, days ago, type)
- Dream type is strictly "Good" or "Bad"

### Dreams Summary
- Good Dreams = 6
- Bad Dreams = 4
- Total Dreams = 10
- Recurring dreams are derived from diary data (not hard-coded):
  - "Flying over mountains" and "Lost in maze" appear multiple times
  - Calculated recurring count matches the summary page value

### AI Classification (Bonus)
- Each dream is classified via OpenAI API
- AI result compared against website classification

## Screenshots

On test failure, a screenshot is automatically captured and:
- Saved to `screenshots/` directory
- Attached to the Allure report

## Assumptions

- Tests run against the live website at `https://arjitnigam.github.io/myDreams/`
- Chrome browser is used (managed automatically via WebDriverManager)
- The loading animation takes approximately 3 seconds (as per the site's JavaScript)
- Popup blocking is disabled in Chrome options so that `window.open()` calls work
