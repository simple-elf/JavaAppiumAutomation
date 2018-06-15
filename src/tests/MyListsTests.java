package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class MyListsTests extends CoreTestCase {

    @Test
    public void testSaveFirstArticleToMyList() {
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);;

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);;
        //articlePageObject.waitForTitleElement();
        String articleTitle = articlePageObject.getArticleTitle();
        String nameForSavedReadingList = "Learning programming";

        articlePageObject.addArticleToNewReadingList(nameForSavedReadingList);
        articlePageObject.closeOpenedArticle();

        NavigationUI navigationUI = new NavigationUI(driver);
        navigationUI.openMyLists();

        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
        myListsPageObject.openFolderByName(nameForSavedReadingList);
        myListsPageObject.swipeByArticleToDelete(articleTitle);
    }

    @Test
    public void testSaveTwoArticlesToMyListAndRemove() {
        String firstSearch = "Selenium";
        String firstSearchTitle = "Selenium (software)";
        String secondSearch = "Appium";
        String nameForSavedReadingList = "Learning mobile automation";

        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);;

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput(firstSearch);
        searchPageObject.clickByArticleWithSubstring(firstSearchTitle);

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);;
        articlePageObject.waitForTitleElement();
        articlePageObject.addArticleToNewReadingList(nameForSavedReadingList);
        articlePageObject.closeOpenedArticle();


        searchPageObject.initSearchInput();
        searchPageObject.typeSearchInput(secondSearch);
        searchPageObject.clickByArticleWithSubstring(secondSearch);

        articlePageObject.waitForTitleElement();
        articlePageObject.addArticleToExistingReadingList(nameForSavedReadingList);
        articlePageObject.closeOpenedArticle();


        NavigationUI navigationUI = new NavigationUI(driver);
        navigationUI.openMyLists();

        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
        myListsPageObject.openFolderByName(nameForSavedReadingList);
        myListsPageObject.swipeByArticleToDelete(firstSearchTitle);

        // Интересно что сработал тот же метод, который использовался в результатах поиска
        searchPageObject.clickByArticleWithSubstring(secondSearch);
        articlePageObject.waitForTitleElement();
        articlePageObject.closeOpenedArticle();
    }

}
