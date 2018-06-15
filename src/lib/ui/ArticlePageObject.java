package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

abstract public class ArticlePageObject extends MainPageObject {

    protected static By
            TITLE,
            FOOTER_ELEMENT,
            ADD_TO_READING_LIST_BUTTON,
            ONBOARDING,
            MY_LIST_NAME_INPUT,
            MY_LIST_OK_BUTTON,
            CLOSE_ARTICLE_BUTTON;
    protected static String
            SAVED_READING_LIST_BY_NAME_TPL;

    private static By getReadingListLocatorByName(String nameOfReadingList) {
        return By.xpath(SAVED_READING_LIST_BY_NAME_TPL.replace("{NAME}", nameOfReadingList));
    }

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

    public void checkTitleElementImmideatly() {
        this.assertElementPresent(TITLE, "Cannot find article title immediately");
    }

    public WebElement waitForTitleElement() {
        return this.waitForElementPresent(TITLE, "Cannot find article title", 15);
    }

    public String getArticleTitle() {
        WebElement titleElement = this.waitForTitleElement();
        if (Platform.getInstance().isAndroid()) {
            return titleElement.getAttribute("text");
        } else {
            return titleElement.getAttribute("name");
        }

    }

    public void swipeToFooter() {
        if (Platform.getInstance().isAndroid()) {
            this.swipeUpToFindElement(FOOTER_ELEMENT, "Cannot find footer element by swipe", 40);
        } else {
            this.swipeUpToTillElementAppear(FOOTER_ELEMENT, "Cannot find footer element by swipe", 40);
        }
    }

    public void clickAddArticleToReadingList() {
        this.waitForElementAndClick(
                ADD_TO_READING_LIST_BUTTON,
                "Cannot find button for adding article to reading list",
                5);
    }

    public void addArticleToNewReadingList(String nameForSavedReadingList) {
        this.clickAddArticleToReadingList();

        this.waitForElementAndClick(
                ONBOARDING,
                "Cannot find onboarding overlay",
                5);

        this.waitForElementAndClear(
                MY_LIST_NAME_INPUT,
                "Cannot find input for new reading list",
                5);

        this.waitForElementAndSendKeys(
                MY_LIST_NAME_INPUT,
                nameForSavedReadingList,
                "Cannot send text to input for reading list",
                5);

        this.waitForElementAndClick(
                MY_LIST_OK_BUTTON,
                "Cannot press OK button",
                5);
        this.waitForElementNotPresent(
                MY_LIST_OK_BUTTON,
                "OK button still exists",
                5);
    }

    public void addArticleToExistingReadingList(String nameForSavedReadingList) {
        this.clickAddArticleToReadingList();

        this.waitForElementAndClick(
                getReadingListLocatorByName(nameForSavedReadingList),
                "Cannot find existing reading list: " + nameForSavedReadingList,
                5);

        this.waitForElementNotPresent(
                getReadingListLocatorByName(nameForSavedReadingList),
                "Name of saved reading list still exists",
                5);
    }

    public void closeOpenedArticle() {
        this.waitForElementAndClick(CLOSE_ARTICLE_BUTTON, "Cannot find close article button", 5);
    }

}
