import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName","AndroidTestDevice_testtest");
        capabilities.setCapability("platformVersion","8.0");
        capabilities.setCapability("automationName","Appium");
        capabilities.setCapability("appPackage","org.wikipedia");
        capabilities.setCapability("appActivity",".main.MainActivity");
        capabilities.setCapability("app","C:\\Users\\simpl\\IdeaProjects\\JavaAppiumAutomation\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void firstTest() {
        waitForElementByXpathAndClick(
                "//*[contains(@text, 'Search Wikipedia')]",
                "Cannot find search button",
                5);

        waitForElementByXpathAndSendKeys(
                "//*[contains(@text, 'Search…')]",
                "Java",
                "Cannot find search input" ,
                5);

        waitForElementPresentByXpath(
                "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']",
                "Cannon find Java OOP",
                15);
    }

    private WebElement waitForElementByXpathAndClick(String xpath, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresentByXpath(xpath, errorText, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementByXpathAndSendKeys(String xpath, String value, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresentByXpath(xpath, errorText, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private WebElement waitForElementPresentByXpath(String xpath, String errorText) {
        return waitForElementPresentByXpath(xpath, errorText, 5);
    }

    private WebElement waitForElementPresentByXpath(String xpath, String errorText, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorText + "\n");
        By by = By.xpath(xpath);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

}
