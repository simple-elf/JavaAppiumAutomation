package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPageObject extends MainPageObject {

    private static final By
            SEARCH_INIT_ELEMENT = By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
            SEARCH_INPUT = By.id("org.wikipedia:id/search_src_text"),
            SEARCH_CANCEL_BUTTON = By.id("org.wikipedia:id/search_close_btn"),
            SEARCH_EMPTY_MESSAGE = By.id("org.wikipedia:id/search_empty_message"),
            SEARCH_RESULTS_LIST = By.id("org.wikipedia:id/search_results_list"),
            SEARCH_RESULTS_LIST_ITEM = By.id("org.wikipedia:id/page_list_item_container"),
            SEARCH_RESULTS_LIST_ITEM_TITLE = By.id("org.wikipedia:id/page_list_item_title"),
            EMPTY_RESULT_LABEL = By.xpath("//*[@text='No results found']"),
            SEARCH_RESULTS_ELEMENT = By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']" +
                    "/*[@resource-id='org.wikipedia:id/page_list_item_container']");
    private static final String
            SEARCH_EMPTY_MESSAGE_TEXT = "Search and read the free encyclopedia in your language",
            SEARCH_RESULT_BY_SUBSTRING_TPL =
            "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']",
            SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION =
                    "//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{TITLE}']/" +
                    "../*[@resource-id='org.wikipedia:id/page_list_item_description'][@text='{DESCRIPTION}']";

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    /* TEMPLATE METHODS */
    private static By getResultSearchElement(String subString) {
        return By.xpath(SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", subString));
    }

    private static By getResultSearchElementByTitleAndDescription(String title, String description) {
        return By.xpath(SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION
                .replace("{TITLE}", title)
                .replace("{DESCRIPTION}", description));
    }
    /* TEMPLATE METHODS */

    public void initSearchInput() {
        this.waitForElementAndClick(
                SEARCH_INIT_ELEMENT,
                "Cannot find and click search button",
                5);
        WebElement searchInput = this.waitForElementPresent(
                SEARCH_INPUT,
                "Cannot find search input",
                5);
        this.checkElementText(searchInput, "Search…");
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(
                SEARCH_CANCEL_BUTTON,
                "Cannot find search cancel button",
                5);
    }

    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(
                SEARCH_CANCEL_BUTTON,
                "Search cancel button is still present",
                5);
    }

    public void clickCancelSearch() {
        this.waitForElementAndClick(
                SEARCH_CANCEL_BUTTON,
                "Cannot find and click search cancel button",
                5);
    }

    public void typeSearchInput(String searchString) {
        this.waitForElementAndSendKeys(
                SEARCH_INPUT,
                searchString,
                "Cannot find and type into search input",
                5);
    }

    public void clearSearchInput() {
        this.waitForElementAndClear(
                SEARCH_INPUT,
                "Cannot find and clear search input",
                5);
    }

    public void waitForSearchResult(String subString) {
        this.waitForElementPresent(
                getResultSearchElement(subString),
                "Cannon find search result with substring " + subString,
                15);
    }

    public void clickByArticleWithSubstring(String subString) {
        this.waitForElementAndClick(
                getResultSearchElement(subString),
                "Cannon find and click search result with substring " + subString,
                10);
    }

    public void waitForSearchResultsListNotEmpty() {
        List<WebElement> searchResults = getSearchResultsList();
        System.out.println("Size: " + searchResults.size());
        Assert.assertTrue("There is no search results", searchResults.size() > 0);
    }

    public List<WebElement> getSearchResultsList() {
        WebElement searchResultsList = this.waitForElementPresent(
                SEARCH_RESULTS_LIST,
                "Cannot find search results list",
                15);
        return searchResultsList.findElements(SEARCH_RESULTS_LIST_ITEM);
    }

    public String getSearchResultItemTitle(WebElement searchResultItem) {
        return searchResultItem.findElement(SEARCH_RESULTS_LIST_ITEM_TITLE).getText();
    }

    public void waitForSearchEmptyMessage() {
        WebElement emptyMessage = this.waitForElementPresent(
                SEARCH_EMPTY_MESSAGE,
                "Cannot find empty search message",
                5);
        this.checkElementText(emptyMessage, SEARCH_EMPTY_MESSAGE_TEXT);
    }

    public int getAmountOfFoundArticles() {
        this.waitForElementPresent(
                SEARCH_RESULTS_ELEMENT,
                "Cannot find search results list",
                15);

        return this.getAmountOfElements(SEARCH_RESULTS_ELEMENT);
    }

    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(
                EMPTY_RESULT_LABEL,
                "Cannot find empty result label",
                15);
    }

    public void assertThereIsNoResultOfSearch() {
        this.assertElementNotPresent(
                SEARCH_RESULTS_ELEMENT,
                "We supposed not to find any results");
    }

    public void searchAndCheckResults(String searchString) {
        this.initSearchInput();
        this.typeSearchInput(searchString);
        this.waitForSearchResultsListNotEmpty();

        List<WebElement> searchResultsList = this.getSearchResultsList();
        searchResultsList.remove(searchResultsList.size() - 1); // fix to remove last partly visible line
        System.out.println("Size: " + searchResultsList.size());

        for (WebElement searchItem : searchResultsList) {
            String searchItemTitle = this.getSearchResultItemTitle(searchItem);
            System.out.println("searchItemTitle: " + searchItemTitle);
            Assert.assertThat(
                    "Search result item doesn't contains search text",
                    searchItemTitle,
                    CoreMatchers.containsString(searchString));
        }

        this.clearSearchInput();
        this.waitForSearchEmptyMessage();
    }

    public void waitForElementByTitleAndDescription(String title, String description) {
        this.waitForElementPresent(
                getResultSearchElementByTitleAndDescription(title, description),
                "Cannot find search result by title '" + title + "' and description '" + description + "'",
                5);
    }

}
