package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SearchPageObject extends MainPageObject {

    private static final String
            SEARCH_INIT_ELEMENT = "//*[contains(@text, 'Search Wikipedia')]",
            SEARCH_INPUT = "org.wikipedia:id/search_src_text",
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

    public void typeSearchInput(String searchString) {
        this.waitForElementAndSendKeys(By.id(SEARCH_INPUT), searchString, "Cannot find and type into search input", 5);
    }

    public void waitForSearchResult(String subString) {
        this.waitForElementPresent(By.xpath(getResultSearchElement(subString)), "Cannon find search result with substring " + subString, 15);
    }

}
