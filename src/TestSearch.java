import io.appium.java_client.TouchAction;
import lib.CoreTestCase;
import lib.ui.*;
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
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput("Java");
        searchPageObject.waitForSearchResultsListNotEmpty();

        searchPageObject.clearSearchInput();
        searchPageObject.waitForSearchEmptyMessage();

        searchPageObject.waitForCancelButtonToAppear();
        searchPageObject.clickCancelSearch();
        searchPageObject.waitForCancelButtonToDisappear();
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
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        String articleTitle = articlePageObject.getArticleTitle();

        Assert.assertEquals("Element text is not expected!", "Java (programming language)", articleTitle);
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
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput("Appium");
        searchPageObject.clickByArticleWithSubstring("Appium");

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        articlePageObject.waitForTitleElement();
        articlePageObject.swipeToFooter();
    }

    @Test
    public void testSaveTwoArticlesToMyListAndRemove() {
        String firstSearch = "Selenium";
        String firstSearchTitle = "Selenium (software)";
        String secondSearch = "Appium";
        String nameForSavedReadingList = "Learning mobile automation";

        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput(firstSearch);
        searchPageObject.clickByArticleWithSubstring(firstSearchTitle);

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        articlePageObject.waitForTitleElement();
        articlePageObject.addArticleToNewReadingList(nameForSavedReadingList);
        articlePageObject.closeOpenedArticle();


        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput(secondSearch);
        searchPageObject.clickByArticleWithSubstring(secondSearch);

        articlePageObject.waitForTitleElement();
        articlePageObject.addArticleToExistingReadingList(nameForSavedReadingList);
        articlePageObject.closeOpenedArticle();


        NavigationUI navigationUI = new NavigationUI(driver);
        navigationUI.openMyLists();

        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
        myListsPageObject.openFolderByName(nameForSavedReadingList);
        myListsPageObject.swipeByArticleToDelete(firstSearchTitle);

        // Интересно что сработал тот же метод, который использовался в результатах поиска
        searchPageObject.clickByArticleWithSubstring(secondSearch);
        articlePageObject.waitForTitleElement();
        articlePageObject.closeOpenedArticle();
    }

    @Test
    public void testSaveFirstArticleToMyList() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        //articlePageObject.waitForTitleElement();
        String articleTitle = articlePageObject.getArticleTitle();
        String nameForSavedReadingList = "Learning programming";

        articlePageObject.addArticleToNewReadingList(nameForSavedReadingList);
        articlePageObject.closeOpenedArticle();

        NavigationUI navigationUI = new NavigationUI(driver);
        navigationUI.openMyLists();

        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
        myListsPageObject.openFolderByName(nameForSavedReadingList);
        myListsPageObject.swipeByArticleToDelete(articleTitle);
    }

}
