import io.appium.java_client.TouchAction;
import lib.CoreTestCase;
import lib.ui.MainPageObject;
import lib.ui.SearchPageObject;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TestSearch extends CoreTestCase {

    private MainPageObject mainPageObject;

    protected void setUp() throws Exception {
        super.setUp();

        mainPageObject = new MainPageObject(driver);
    }

    @Test
    public void testSearchAndCheck() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");
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
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5);

        WebElement searchResultsList = mainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "Cannot find search results list",
                15);

        List<WebElement> searchResults = searchResultsList.findElements(By.className("android.widget.LinearLayout"));
        System.out.println("Size: " + searchResults.size());
        Assert.assertTrue("There is no search results", searchResults.size() > 0);

        mainPageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search input",
                5);

        WebElement emptyMessage = mainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_empty_message"),
                "Cannot find empty message",
                5);
        mainPageObject.checkElementText(emptyMessage, "Search and read the free encyclopedia in your language");

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find close button",
                5);

        mainPageObject.waitForElementNotPresent(
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
        mainPageObject.searchAndCheckResults("Java");
        mainPageObject.searchAndCheckResults("Selenium");

        mainPageObject.searchAndCheckResults("Android"); // fixed
        mainPageObject.searchAndCheckResults("Appium"); // fails because second item is AppImage
    }

    @Test
    public void testAmountOfNotEmptySearch() {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        String searchString = "Avril Lavigne discography";
        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchString,
                "Cannot find search input",
                5);

        String searchResultLocator = "//*[@resource-id='org.wikipedia:id/search_results_list']" +
                "/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        mainPageObject.waitForElementPresent(
                By.xpath(searchResultLocator),
                "Cannot find search results list",
                15);

        int amountOfSearchResults = mainPageObject.getAmountOfElements(By.xpath(searchResultLocator));

        Assert.assertTrue("We found zero results", amountOfSearchResults > 0);
    }

    @Test
    public void testAmountOfEmptySearch() {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        String searchString = "Selenium Selenide Appium";
        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchString,
                "Cannot find search input",
                5);

        String searchResultLocator = "//*[@resource-id='org.wikipedia:id/search_results_list']" +
                "/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        String emptyResultLabel = "//*[@text='No results found']";

        mainPageObject.waitForElementPresent(
                By.xpath(emptyResultLabel),
                "Cannot find empty result label",
                15);

        mainPageObject.assertElementNotPresent(
                By.xpath(searchResultLocator),
                "We found some results by request " + searchString);
    }

    @Test
    public void testChangeScreenRotationOnSearchResults() {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        String searchString = "Java";
        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchString,
                "Cannot find search input",
                5);

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "Cannon find Java OOP by search " + searchString,
                15);

        String titleBeforeRotation = mainPageObject.waitForElementAndGetAttr(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                15);

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String titleAfterRotation = mainPageObject.waitForElementAndGetAttr(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                15);

        Assert.assertEquals("Article title have been change after screen rotation", titleBeforeRotation, titleAfterRotation);

        driver.rotate(ScreenOrientation.PORTRAIT);

        String titleAfterSecondRotation = mainPageObject.waitForElementAndGetAttr(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                15);

        Assert.assertEquals("Article title have been change after screen rotation", titleBeforeRotation, titleAfterSecondRotation);
    }

    @Test
    public void testCheckSearchArticleInBackgroud() {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        String searchString = "Java";
        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchString,
                "Cannot find search input",
                5);

        mainPageObject.waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "Cannon find Java OOP by search " + searchString,
                15);

        driver.runAppInBackground(2);

        mainPageObject.waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "Cannon find article 'Java OOP' after returning from background",
                15);
    }

    @Test
    public void testCompareArticleTitle() {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5);

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "Cannon find Java OOP",
                5);

        WebElement titleElement = mainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15);

        //String articleTitle = titleElement.getAttribute("text");
        //Assert.assertEquals("Were are unexpected title!", "Java (programming language)", articleTitle);
        mainPageObject.checkElementText(titleElement, "Java (programming language)");
    }

    @Test
    public void testCheckArticleTitlePresent() {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5);

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "Cannon find Java OOP",
                5);

        //mainPageObject.waitForElementPresent(
        //        By.id("org.wikipedia:id/view_page_title_text"),
        //        "Cannot find article title",
        //        15);

        mainPageObject.assertElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title immediately");
    }

    @Test
    public void testSwipeArticle() {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Appium",
                "Cannot find search input",
                5);

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
                "Cannon find Appium",
                5);

        mainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15);

        mainPageObject.swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Cannot find footer element",
                10);

    }

    @Test
    public void testSaveTwoArticlesToMyListAndRemove() {
        String firstSearch = "Selenium";
        String firstSearchTitle = "Selenium (software)";
        String secondSearch = "Appium";
        String nameForSavedReadingList = "Learning mobile automation";

        mainPageObject.startSearch(firstSearch);
        mainPageObject.checkSearchResultAndOpen(firstSearchTitle);
        mainPageObject.addArticleToNewReadingList(nameForSavedReadingList);
        mainPageObject.closeOpenedArticle();

        mainPageObject.startSearch(secondSearch);
        mainPageObject.checkSearchResultAndOpen(secondSearch);
        mainPageObject.addArticleToExistingReadingList(nameForSavedReadingList);
        mainPageObject.closeOpenedArticle();

        mainPageObject.openSavedReadingList(nameForSavedReadingList);
        mainPageObject.deleteSavedArticleFromReadingList(firstSearchTitle);

        // Интересно что сработал тот же метод, который использовался в результатах поиска
        mainPageObject.checkSearchResultAndOpen(secondSearch);
    }

    @Test
    public void testSaveFirstArticleToMyList() {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input",
                5);

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "Cannon find Java OOP",
                5);

        mainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15);

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find options button",
                5);

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find option for adding to reading list",
                5);

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find onboarding overlay",
                5);

        mainPageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find input for new reading list",
                5);

        String nameForSavedReadingList = "Learning programming";

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                nameForSavedReadingList,
                "Cannot send text to input for reading list",
                5);

        System.out.println("Press OK button");
        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Cannot press OK button",
                5);
        mainPageObject.waitForElementNotPresent(
                By.xpath("//*[@text='OK']"),
                "OK button still exists",
                5);

        mainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15);

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot find close article button",
                5);

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find button 'My lists'",
                5);

        mainPageObject.waitForElementPresent(
                By.xpath("//android.widget.TextView[@text='My lists']"),
                "Cannot find saved lists header",
                5);

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + nameForSavedReadingList + "']"),
                "Cannon find saved reading list",
                5);

        mainPageObject.swipeElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot swipe saved article");

        mainPageObject.waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot delete saved article",
                5);
    }

}
