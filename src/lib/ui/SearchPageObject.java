package lib.ui;

import io.appium.java_client.AppiumDriver;
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
            SEARCH_RESULTS_LIST_ITEM = "android.widget.LinearLayout",
            SEARCH_RESULT_BY_SUBSTRING_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']";

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    /* TEMPLATE METHODS */
    private static String getResultSearchElement(String subString) {
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", subString);
    }
    /* TEMPLATE METHODS */

    public void initSearchInput() {
        this.waitForElementAndClick(By.xpath(SEARCH_INIT_ELEMENT), "Cannot find and click search button", 5);
        WebElement searchInput = this.waitForElementPresent(By.id(SEARCH_INPUT), "Cannot find search input");
        this.checkElementText(searchInput, "Search…");
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(By.id(SEARCH_CANCEL_BUTTON), "Cannot find search cancel button", 5);
    }

    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(By.id(SEARCH_CANCEL_BUTTON), "Search cancel button is still present", 5);
    }

    public void clickCancelSearch() {
        this.waitForElementAndClick(By.id(SEARCH_CANCEL_BUTTON), "Cannot find and click search cancel button", 5);
    }

    public void typeSearchInput(String searchString) {
        this.waitForElementAndSendKeys(By.id(SEARCH_INPUT), searchString, "Cannot find and type into search input", 5);
    }

    public void clearSearchInput() {
        this.waitForElementAndClear(By.id(SEARCH_INPUT), "Cannot find and clear search input", 5);
    }

    public void waitForSearchResult(String subString) {
        this.waitForElementPresent(By.xpath(getResultSearchElement(subString)), "Cannon find search result with substring " + subString, 15);
    }

    public void clickByArticleWithSubstring(String subString) {
        this.waitForElementAndClick(By.xpath(getResultSearchElement(subString)), "Cannon find and click search result with substring " + subString, 10);
    }

    public void waitForSearchResultsListNotEmpty() {
        WebElement searchResultsList =
                this.waitForElementPresent(By.id(SEARCH_RESULTS_LIST), "Cannot find search results list", 15);
        List<WebElement> searchResults = searchResultsList.findElements(By.className(SEARCH_RESULTS_LIST_ITEM));
        System.out.println("Size: " + searchResults.size());
        Assert.assertTrue("There is no search results", searchResults.size() > 0);
    }

    public void waitForSearchEmptyMessage() {
        WebElement emptyMessage =
                this.waitForElementPresent(By.id(SEARCH_EMPTY_MESSAGE), "Cannot find empty search message", 5);
        this.checkElementText(emptyMessage, SEARCH_EMPTY_MESSAGE_TEXT);
    }

}
