package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyListsPageObject extends MainPageObject {

    public static final String
            READING_LIST_BY_NAME_TPL = "//*[@text='{READING_LIST_NAME}']",
            MY_LISTS_HEADER = "//android.widget.TextView[@text='My lists']",
            ARTICLE_BY_TITLE_TPL = "//*[@text='{TITLE}']";

    private static String getFolderXpathByName(String nameOfFolder) {
        return READING_LIST_BY_NAME_TPL.replace("{READING_LIST_NAME}", nameOfFolder);
    }

    private static String getSavedArticleXpathByTitle(String title) {
        return ARTICLE_BY_TITLE_TPL.replace("{TITLE}", title);
    }

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    public void openFolderByName(String nameOfReadingList) {
        this.waitForMyListsPageHeader();
        this.waitForElementAndClick(
                By.xpath(getFolderXpathByName(nameOfReadingList)),
                "Cannon find saved reading list by name: " + nameOfReadingList,
                5);
    }

    public void waitForMyListsPageHeader() {
        this.waitForElementPresent(
                By.xpath(MY_LISTS_HEADER),
                "Cannot find saved lists header",
                5);
    }

    public void waitForArticleToAppearByTitle(String articleTitle) {
        this.waitForElementPresent(
                By.xpath(getSavedArticleXpathByTitle(articleTitle)),
                "Cannot find saved article: " + articleTitle,
                5);
    }

    public void waitForArticleToDisappearByTitle(String articleTitle) {
        this.waitForElementNotPresent(
                By.xpath(getSavedArticleXpathByTitle(articleTitle)),
                "Cannot delete saved article: " + articleTitle,
                5);
    }

    public void swipeByArticleToDelete(String articleTitle) {
        this.waitForArticleToAppearByTitle(articleTitle);

        this.swipeElementToLeft(
                By.xpath(getSavedArticleXpathByTitle(articleTitle)),
                "Cannot swipe saved article: " + articleTitle);

        this.waitForArticleToDisappearByTitle(articleTitle);
    }

}
