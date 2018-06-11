package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPageObject extends MainPageObject {

    private static final String
            SEARCH_INIT_ELEMENT = "//*[contains(@text, 'Search Wikipedia')]",
            SEARCH_INPUT = "org.wikipedia:id/search_src_text",
            SEARCH_CANCEL_BUTTON = "org.wikipedia:id/search_close_btn",
            SEARCH_EMPTY_MESSAGE = "org.wikipedia:id/search_empty_message",
            SEARCH_EMPTY_MESSAGE_TEXT = "Search and read the free encyclopedia in your language",
            SEARCH_RESULTS_LIST = "org.wikipedia:id/search_results_list",
            //SEARCH_RESULTS_LIST_ITEM = "android.widget.LinearLayout",
            SEARCH_RESULTS_LIST_ITEM = "org.wikipedia:id/page_list_item_container", // fix search locator
            SEARCH_RESULTS_LIST_ITEM_TITLE = "org.wikipedia:id/page_list_item_title",
            SEARCH_RESULT_BY_SUBSTRING_TPL =
                    "//*[@resource-id='" + SEARCH_RESULTS_LIST_ITEM + "']//*[@text='{SUBSTRING}']",
            SEARCH_RESULTS_ELEMENT = "//*[@resource-id='" + SEARCH_RESULTS_LIST + "']" +
                    "/*[@resource-id='" + SEARCH_RESULTS_LIST_ITEM + "']",
            SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION =
                    "//*[@resource-id='" + SEARCH_RESULTS_LIST_ITEM + "']" +
                    "//*[@resource-id='" + SEARCH_RESULTS_LIST_ITEM_TITLE + "'][@text='{TITLE}']/" +
                    "../*[@resource-id='org.wikipedia:id/page_list_item_description'][@text='{DESCRIPTION}']",
            EMPTY_RESULT_LABEL = "//*[@text='No results found']";

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    /* TEMPLATE METHODS */
    private static String getResultSearchElement(String subString) {
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", subString);
    }

    private static String getResultSearchElementByTitleAndDescription(String title, String description) {
        return SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION
                .replace("{TITLE}", title)
                .replace("{DESCRIPTION}", description);
    }
    /* TEMPLATE METHODS */

    public void initSearchInput() {
        this.waitForElementAndClick(
                By.xpath(SEARCH_INIT_ELEMENT),
                "Cannot find and click search button",
                5);
        WebElement searchInput = this.waitForElementPresent(
                By.id(SEARCH_INPUT),
                "Cannot find search input",
                5);
        this.checkElementText(searchInput, "Search…");
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(
                By.id(SEARCH_CANCEL_BUTTON),
                "Cannot find search cancel button",
                5);
    }

    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(
                By.id(SEARCH_CANCEL_BUTTON),
                "Search cancel button is still present",
                5);
    }

    public void clickCancelSearch() {
        this.waitForElementAndClick(
                By.id(SEARCH_CANCEL_BUTTON),
                "Cannot find and click search cancel button",
                5);
    }

    public void typeSearchInput(String searchString) {
        this.waitForElementAndSendKeys(
                By.id(SEARCH_INPUT),
                searchString,
                "Cannot find and type into search input",
                5);
    }

    public void clearSearchInput() {
        this.waitForElementAndClear(
                By.id(SEARCH_INPUT),
                "Cannot find and clear search input",
                5);
    }

    public void waitForSearchResult(String subString) {
        this.waitForElementPresent(
                By.xpath(getResultSearchElement(subString)),
                "Cannon find search result with substring " + subString,
                15);
    }

    public void clickByArticleWithSubstring(String subString) {
        this.waitForElementAndClick(
                By.xpath(getResultSearchElement(subString)),
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
                By.id(SEARCH_RESULTS_LIST),
                "Cannot find search results list",
                15);
        return searchResultsList.findElements(By.id(SEARCH_RESULTS_LIST_ITEM));
    }

    public String getSearchResultItemTitle(WebElement searchResultItem) {
        return searchResultItem.findElement(By.id(SEARCH_RESULTS_LIST_ITEM_TITLE)).getText();
    }

    public void waitForSearchEmptyMessage() {
        WebElement emptyMessage = this.waitForElementPresent(
                By.id(SEARCH_EMPTY_MESSAGE),
                "Cannot find empty search message",
                5);
        this.checkElementText(emptyMessage, SEARCH_EMPTY_MESSAGE_TEXT);
    }

    public int getAmountOfFoundArticles() {
        this.waitForElementPresent(
                By.xpath(SEARCH_RESULTS_ELEMENT),
                "Cannot find search results list",
                15);

        return this.getAmountOfElements(By.xpath(SEARCH_RESULTS_ELEMENT));
    }

    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(
                By.xpath(EMPTY_RESULT_LABEL),
                "Cannot find empty result label",
                15);
    }

    public void assertThereIsNoResultOfSearch() {
        this.assertElementNotPresent(
                By.xpath(SEARCH_RESULTS_ELEMENT),
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
                By.xpath(getResultSearchElementByTitleAndDescription(title, description)),
                "Cannot find search result by title '" + title + "' and description '" + description + "'",
                5);
    }

}
