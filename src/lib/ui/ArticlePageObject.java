package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject {

    private static final String
            TITLE = "org.wikipedia:id/view_page_title_text",
            FOOTER_ELEMENT = "//*[@text='View page in browser']",
            ADD_TO_READING_LIST_BUTTON = "//android.widget.ImageView[@content-desc='Add this article to a reading list']",
            ONBOARDING = "org.wikipedia:id/onboarding_button",
            MY_LIST_NAME_INPUT = "org.wikipedia:id/text_input",
            MY_LIST_OK_BUTTON = "//*[@text='OK']",
            CLOSE_ARTICLE_BUTTON = "//android.widget.ImageButton[@content-desc='Navigate up']",
            SAVED_READING_LIST_BY_NAME_TPL = "//android.widget.TextView[@text='{NAME}']";

    private static String getReadingListXpathByName(String nameOfReadingList) {
        return SAVED_READING_LIST_BY_NAME_TPL.replace("{NAME}", nameOfReadingList);
    }

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

    public WebElement waitForTitleElement() {
        return this.waitForElementPresent(By.id(TITLE), "Cannot find article title", 15);
    }

    public String getArticleTitle() {
        WebElement titleElement = this.waitForTitleElement();
        return titleElement.getAttribute("text");
    }

    public void swipeToFooter() {
        this.swipeUpToFindElement(By.xpath(FOOTER_ELEMENT), "Cannot find footer element by swipe", 10);
    }

    public void clickAddArticleToReadingList() {
        this.waitForElementAndClick(
                By.xpath(ADD_TO_READING_LIST_BUTTON),
                "Cannot find button for adding article to reading list",
                5);
    }

    public void addArticleToNewReadingList(String nameForSavedReadingList) {
        this.clickAddArticleToReadingList();

        this.waitForElementAndClick(
                By.id(ONBOARDING),
                "Cannot find onboarding overlay",
                5);

        this.waitForElementAndClear(
                By.id(MY_LIST_NAME_INPUT),
                "Cannot find input for new reading list",
                5);

        this.waitForElementAndSendKeys(
                By.id(MY_LIST_NAME_INPUT),
                nameForSavedReadingList,
                "Cannot send text to input for reading list",
                5);

        //System.out.println("Press OK button");
        this.waitForElementAndClick(
                By.xpath(MY_LIST_OK_BUTTON),
                "Cannot press OK button",
                5);
        this.waitForElementNotPresent(
                By.xpath(MY_LIST_OK_BUTTON),
                "OK button still exists",
                5);
    }

    public void addArticleToExistingReadingList(String nameForSavedReadingList) {
        this.clickAddArticleToReadingList();

        this.waitForElementAndClick(
                By.xpath(getReadingListXpathByName(nameForSavedReadingList)),
                "Cannot find existing reading list: " + nameForSavedReadingList,
                5);

        this.waitForElementNotPresent(
                By.xpath(getReadingListXpathByName(nameForSavedReadingList)),
                "Name of saved reading list still exists",
                5);
    }

    public void closeOpenedArticle() {
        this.waitForElementAndClick(
                By.xpath(CLOSE_ARTICLE_BUTTON),
                "Cannot find close article button",
                5);
    }

}
