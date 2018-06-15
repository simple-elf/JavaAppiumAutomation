package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.ArticlePageObject;
import org.openqa.selenium.By;

public class iOSArticlePageObject extends ArticlePageObject {

    static {
        TITLE = By.id("Appium"); // By.id("Java (programming language)");
        FOOTER_ELEMENT = By.id("View article in browser");
        ADD_TO_READING_LIST_BUTTON = By.id("Save for later");
        CLOSE_ARTICLE_BUTTON = By.id("Back");
    }

    public iOSArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

}
