package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject {

    private static final By
            TITLE = By.id("org.wikipedia:id/view_page_title_text"),
            FOOTER_ELEMENT = By.xpath("//*[@text='View page in browser']"),
            ADD_TO_READING_LIST_BUTTON = By.xpath("//android.widget.ImageView[@content-desc='Add this article to a reading list']"),
            ONBOARDING = By.id("org.wikipedia:id/onboarding_button"),
            MY_LIST_NAME_INPUT = By.id("org.wikipedia:id/text_input"),
            MY_LIST_OK_BUTTON = By.xpath("//*[@text='OK']"),
            CLOSE_ARTICLE_BUTTON = By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");
    private static final String
            SAVED_READING_LIST_BY_NAME_TPL = "//android.widget.TextView[@text='{NAME}']";

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
        return titleElement.getAttribute("text");
    }

    public void swipeToFooter() {
        this.swipeUpToFindElement(FOOTER_ELEMENT, "Cannot find footer element by swipe", 10);
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
