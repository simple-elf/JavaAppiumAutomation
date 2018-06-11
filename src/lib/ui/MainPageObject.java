package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPageObject {

    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }

    public String waitForElementAndGetAttr(By by, String attr, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorText, timeoutInSeconds);
        return element.getAttribute(attr);
    }

    public void assertElementNotPresent(By by, String errorText) {
        int amountOfElements = getAmountOfElements(by);
        if (amountOfElements > 0) {
            String defaultMessage = "An element '" + by.toString() + "' supposed to be not present";
            throw new AssertionError(defaultMessage + " " + errorText);
        }
    }

    public int getAmountOfElements(By by) {
        List elements = driver.findElements(by);
        return elements.size();
    }

    public void deleteSavedArticleFromReadingList(String articleTitle) {
        swipeElementToLeft(
                By.xpath("//*[@text='" + articleTitle + "']"),
                "Cannot swipe saved article");

        waitForElementNotPresent(
                By.xpath("//*[@text='" + articleTitle + "']"),
                "Cannot delete saved article",
                5);
    }

    public void checkSearchResultAndOpen(String articleTitle) {
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title']" +
                        "[@text='" + articleTitle + "']"),
                "Cannon find searched article in results: '" + articleTitle + "'",
                10);

        WebElement titleElement = waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15);

        checkElementText(titleElement, articleTitle);
    }

    public void startSearch(String searchString) {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search button",
                5);

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchString,
                "Cannot find search input",
                5);

        waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "Cannot find search results list",
                15);
    }

    public void swipeElementToLeft(By by, String errorText) {
        WebElement element = waitForElementPresent(
                by,
                errorText,
                10);

        int leftX = element.getLocation().getX();
        int rightX = leftX + element.getSize().getWidth();
        int upperY = element.getLocation().getY();
        int lowerY = upperY + element.getSize().getHeight();
        int middleY = (upperY + lowerY) / 2;

        new TouchAction(driver)
                .press(rightX, middleY)
                .waitAction(150)
                .moveTo(leftX, middleY)
                .release()
                .perform();
    }

    public void swipeUpToFindElement(By by, String errorText, int maxSwipes) {
        int alreadySwipe = 0;
        while (driver.findElements(by).size() == 0) {
            if (alreadySwipe > maxSwipes) {
                waitForElementPresent(by, "Cannot find element by swiping up. \n" + errorText, 0);
                return;
            }
            swipeUpQuick();
            alreadySwipe++;
        }
    }

    protected void swipeUpQuick() {
        swipeUp(100);
    }

    protected void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        action
                .press(x, startY)
                .waitAction(timeOfSwipe)
                .moveTo(x, endY)
                .release()
                .perform();
    }

    public void assertElementPresent(By by, String errorText) {
        // По опыту, метод findElement все равно ждет какой то стандартный таймаут. Нам же нужно проверить моментально
        // Поэтому проверка идет по количеству найденных findElements
        // Тем более что у нас уже есть метод для проверки количества элементов
        int amountOfElements = getAmountOfElements(by);
        if (amountOfElements == 0) {
            String defaultMessage = "An element '" + by.toString() + "' supposed to be present";
            throw new AssertionError(defaultMessage + " " + errorText);
        }
    }

    public void searchAndCheckResults(String searchString) {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput(searchString);

        WebElement searchResults = waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "Cannot find search results list",
                15);

        List<WebElement> searchResultsList = searchResults.findElements(By.id("org.wikipedia:id/page_list_item_container")); // fix search locator
        searchResultsList.remove(searchResultsList.size() - 1);
        System.out.println("Size: " + searchResultsList.size());

        for (WebElement searchItem : searchResultsList) {
            String searchItemTitle = searchItem.findElement(By.id("org.wikipedia:id/page_list_item_title")).getText();
            System.out.println("searchItemTitle: " + searchItemTitle);
            Assert.assertThat("Search result item doesn't contains search text", searchItemTitle, CoreMatchers.containsString(searchString));
        }

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search input",
                5);

        WebElement emptyMessage = waitForElementPresent(
                By.id("org.wikipedia:id/search_empty_message"),
                "Cannot find empty message",
                5);
    }

    /**
     * Ex 2: Написать функцию, которая проверяет наличие текста “Search…” в строке поиска
     * перед вводом текста и помечает тест упавшим, если такого текста нет.
     */
    public void checkElementText(WebElement element, String expectedText) {
        //String actualText = element.getAttribute("text");
        String actualText = element.getText();
        Assert.assertEquals("Element text is not expected!", expectedText, actualText);
    }

    public WebElement waitForElementAndClear(By by, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorText, timeoutInSeconds);
        element.clear();
        return element;
    }

    public WebElement waitForElementAndClick(By by, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorText, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(By by, String value, String errorText, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorText, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    public WebElement waitForElementPresent(By by, String errorText) {
        return waitForElementPresent(by, errorText, 5);
    }

    public WebElement waitForElementPresent(By by, String errorText, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorText + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public boolean waitForElementNotPresent(By by, String errorText, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorText + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

}
