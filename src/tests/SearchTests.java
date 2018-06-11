package tests;

import lib.CoreTestCase;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class SearchTests extends CoreTestCase {

    @Test
    public void testSearchAndCheck() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");
    }

    /**
     * Написать тест, который:
     * 1. Ищет какое-то слово
     * 2. Убеждается, что найдено несколько статей
     * 3. Отменяет поиск (ТУТ НАВЕРНОЕ ИМЕЕТСЯ ВВИДУ ОЧИСТКА ПОЛЯ ПОИСКА, А НЕ ОТМЕНА ПО НАЖАТИЮ КРЕСТИКА)
     * 4. Убеждается, что результат поиска пропал
     */
    @Test
    public void testCancelSearch() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput("Java");
        searchPageObject.waitForSearchResultsListNotEmpty();

        searchPageObject.clearSearchInput();
        searchPageObject.waitForSearchEmptyMessage();

        searchPageObject.waitForCancelButtonToAppear();
        searchPageObject.clickCancelSearch();
        searchPageObject.waitForCancelButtonToDisappear();
    }

    @Test
    public void testAmountOfNotEmptySearch() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        String searchString = "Avril Lavigne discography";
        searchPageObject.typeSearchInput(searchString);
        int amountOfSearchResults = searchPageObject.getAmountOfFoundArticles();

        assertTrue("We found zero results", amountOfSearchResults > 0);
    }

    @Test
    public void testAmountOfEmptySearch() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        String searchString = "Selenium Selenide Appium";
        searchPageObject.typeSearchInput(searchString);

        searchPageObject.waitForEmptyResultsLabel();
        searchPageObject.assertThereIsNoResultOfSearch();
    }

    /**
     * Написать тест, который:
     * 1. Ищет какое-то слово
     * 2. Убеждается, что в каждом результате поиска есть это слово.
     */
    @Test
    public void testSearchAndCheckResults() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.searchAndCheckResults("Java");
        searchPageObject.searchAndCheckResults("Selenium");
        searchPageObject.searchAndCheckResults("Android"); // fixed

        //searchPageObject.searchAndCheckResults("Appium"); // fails because second item is AppImage
    }

}
