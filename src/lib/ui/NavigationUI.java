package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class NavigationUI extends MainPageObject {

    private static final By
            MY_LISTS_LINK = By.xpath("//android.widget.FrameLayout[@content-desc='My lists']");

    public NavigationUI(AppiumDriver driver) {
        super(driver);
    }

    public void openMyLists() {
        this.waitForElementAndClick(
                MY_LISTS_LINK,
                "Cannot find button 'My lists'",
                5);
    }

}
