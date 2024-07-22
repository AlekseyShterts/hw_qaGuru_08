import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

public class WikipediaWebTests {

    @BeforeAll
    public static void settingsTest() {
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
    }

    @BeforeEach
    void setUp() {
        open("https://ru.wikipedia.org");
        $(".main-top-header").shouldHave(text("Добро пожаловать в"));
    }

    @AfterEach
    public void settingsAfterTest() {
        closeWebDriver();
    }


    @ValueSource(strings = {
            "Чебурек", "Беляш", "Шашлык"
    })
    @Tag("REGRESS")
    @ParameterizedTest(name = "Для поиского запроса {0} должен происходить переход на страницу с информацией о {0}")
    void checkGoNextPageAfterSearch(String searchQuery) {
        $(".vector-search-box-input").setValue(searchQuery).pressEnter();
        $(".mw-page-title-main").shouldHave(text(searchQuery));
    }

    @CsvFileSource(resources = "/testData/languageWikiTestData.csv")
    @Tag("WEB")
    @ParameterizedTest(name = "Переход на страницы Википедии для языка {0}")
    void checkChangeLanguagePage(String language, String link) {
        $(".interlanguage-link [lang='" + language + "']").click();
        webdriver().shouldHave(url(link));
    }

    @CsvSource(value = {
            "j : Страницы, ссылающиеся на «Заглавная страница»",
            "k : Связанные правки",
            "q : Служебные страницы"
    }, delimiter = ':')
    @Tag("SMOKE")
    @ParameterizedTest(name = "Переход на страницу {1} из блока Инструменты")
    void checkGoPageInBlockOfTools(String pageName, String titlePage) {
        $(".vector-menu-content-list [accesskey='"+pageName+"']").click();
        $(".firstHeading").shouldHave(text(titlePage));
    }

}
