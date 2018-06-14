package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyListsPageObject extends MainPageObject {

    public static final By

            MY_LISTS_HEADER = By.xpath("//android.widget.TextView[@text='My lists']");
    public static final String
            READING_LIST_BY_NAME_TPL = "//*[@text='{READING_LIST_NAME}']",
            ARTICLE_BY_TITLE_TPL = "//*[@text='{TITLE}']";

    private static By getFolderLocatorByName(String nameOfFolder) {
        return By.xpath(READING_LIST_BY_NAME_TPL.replace("{READING_LIST_NAME}", nameOfFolder));
    }

    private static By getSavedArticleLocatorByTitle(String title) {
        return By.xpath(ARTICLE_BY_TITLE_TPL.replace("{TITLE}", title));
    }

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    public void openFolderByName(String nameOfReadingList) {
        this.waitForMyListsPageHeader();
        this.waitForElementAndClick(
                getFolderLocatorByName(nameOfReadingList),
                "Cannon find saved reading list by name: " + nameOfReadingList,
                5);
    }

    public void waitForMyListsPageHeader() {
        this.waitForElementPresent(
                MY_LISTS_HEADER,
                "Cannot find saved lists header",
                5);
    }

    public void waitForArticleToAppearByTitle(String articleTitle) {
        this.waitForElementPresent(
                getSavedArticleLocatorByTitle(articleTitle),
                "Cannot find saved article: " + articleTitle,
                5);
    }

    public void waitForArticleToDisappearByTitle(String articleTitle) {
        this.waitForElementNotPresent(
                getSavedArticleLocatorByTitle(articleTitle),
                "Cannot delete saved article: " + articleTitle,
                5);
    }

    public void swipeByArticleToDelete(String articleTitle) {
        this.waitForArticleToAppearByTitle(articleTitle);

        this.swipeElementToLeft(
                getSavedArticleLocatorByTitle(articleTitle),
                "Cannot swipe saved article: " + articleTitle);

        this.waitForArticleToDisappearByTitle(articleTitle);
    }

}
