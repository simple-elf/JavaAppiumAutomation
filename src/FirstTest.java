import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName","AndroidTestDevice_testtest");
        capabilities.setCapability("platformVersion","8.0");
        capabilities.setCapability("automationName","Appium");
        capabilities.setCapability("appPackage","org.wikipedia");
        capabilities.setCapability("appActivity",".main.MainActivity");
        capabilities.setCapability("app","C:\\Users\\simpl\\IdeaProjects\\JavaAppiumAutomation\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSearchAndCheck() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find search button",
                5);

        WebElement searchInput = waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search input");
        checkElementText(searchInput, "Search…");

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Cannot find search input",
                5);

        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "Cannon find Java OOP",
                15);
    }

    /**
     * Написать тест, который:
     * 1. Ищет какое-то слово
     * 2. Убеждается, что найдено несколько статей
     * 3. Отменяет поиск (ТУТ НАВЕРНОЕ ИМЕЕТСЯ ВВИДУ ОЧИСТКА ПОЛЯ ПОИСКА, А НЕ ОТМЕНА ПО НАЖАТИЮ КРЕСТИКА)
     * 4. Убеждается, что результат поиска пропал
     */
    @Test
    public void testCancelSearch() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5);

        WebElement searchResultsList = waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "Cannot find search results list",
                15);

        List<WebElement> searchResults = searchResultsList.findElements(By.className("android.widget.LinearLayout"));
        System.out.println("Size: " + searchResults.size());
        Assert.assertTrue("There is no search results", searchResults.size() > 0);

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search input",
                5);

        WebElement emptyMessage = waitForElementPresent(
                By.id("org.wikipedia:id/search_empty_message"),
                "Cannot find empty message",
                5);
        checkElementText(emptyMessage, "Search and read the free encyclopedia in your language");

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find close button",
                5);

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "Close button is still on page",
                5);
    }

    /**
     * Написать тест, который:
     * 1. Ищет какое-то слово
     * 2. Убеждается, что в каждом результате поиска есть это слово.
     */
    @Test
    public void testSearchAndCheckResults() {
        searchAndCheckResults("Java");
        searchAndCheckResults("Selenium");

        searchAndCheckResults("Android"); // fails on last search result with "WebDriverException: Returned value cannot be converted to WebElement: org.wikipedia:id/page_list_item_title"
        searchAndCheckResults("Appium"); // fails because second item is AppImage
    }

    private void searchAndCheckResults(String searchString) {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchString,
                "Cannot find search input",
                5);

        WebElement searchResults = waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "Cannot find search results list",
                15);

        List<WebElement> searchResultsList = searchResults.findElements(By.className("android.widget.LinearLayout"));
        System.out.println("Size: " + searchResultsList.size());

        for (WebElement searchItem : searchResultsList) {
            String searchItemTitle = searchItem.findElement(By.id("org.wikipedia:id/page_list_item_title")).getText();
            System.out.println("searchItemTitle: " + searchItemTitle);
            Assert.assertThat("Search result item doesn't contains search text", searchItemTitle, CoreMatchers.containsString(searchString));
        }

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search input",
                5);

        WebElement emptyMessage = waitForElementPresent(
                By.id("org.wikipedia:id/search_empty_message"),
                "Cannot find empty message",
                5);
    }

    @Test
    public void testCompareArticleTitle() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5);

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "Cannon find Java OOP",
                5);

        WebElement titleElement = waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15);

        //String articleTitle = titleElement.getAttribute("text");
        //Assert.assertEquals("Were are unexpected title!", "Java (programming language)", articleTitle);
        checkElementText(titleElement, "Java (programming language)");
    }

    @Test
    public void testSwipeArticle() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Appium",
                "Cannot find search input",
                5);

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
                "Cannon find Appium",
                5);

        waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15);

        swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Cannot find footer element",
                5);

    }

    protected void swipeUpToFindElement(By by, String errorText, int maxSwipes) {
        int alreadySwipe = 0;
        while (driver.findElements(by).size() == 0) {
            if (alreadySwipe > maxSwipes) {
                waitForElementPresent(by, "Cannot find element by swiping up. \n" + errorText, 0);
                return;
            }
            swipeUpQuick();
            alreadySwipe++;
        }
    }

    protected void swipeUpQuick() {
        swipeUp(100);
    }

    protected void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        action.press(x, startY).waitAction(timeOfSwipe).moveTo(x, endY).release().perform();
    }

    /**
     * Ex 2: Написать функцию, которая проверяет наличие текста “Search…” в строке поиска
     * перед вводом текста и помечает тест упавшим, если такого текста нет.
     */
    private void checkElementText(WebElement element, String expectedText) {
        //String actualText = element.getAttribute("text");
        String actualText = element.getText();
        Assert.assertEquals("Element text is not expected!", expectedText, actualText);
    }

    private WebElement waitForElementAndClear(By by, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorText, timeoutInSeconds);
        element.clear();
        return element;
    }

    private WebElement waitForElementAndClick(By by, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorText, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorText, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private WebElement waitForElementPresent(By by, String errorText) {
        return waitForElementPresent(by, errorText, 5);
    }

    private WebElement waitForElementPresent(By by, String errorText, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorText + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private boolean waitForElementNotPresent(By by, String errorText, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorText + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

}
