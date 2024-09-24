package tests;

import Pages.MainPage;
import Pages.ToDoPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SeleniumTests {

    private WebDriver driver;
    private MainPage mainPage;
    private ToDoPage todoPage;


    @BeforeAll
    public static void setUpClass() {
        //WebDriverManager.chromedriver().setup();
        WebDriverManager.edgedriver().setup();

    }
    
    @BeforeEach
    public void setUp() {
        driver = new EdgeDriver();
        mainPage = new MainPage(driver);
        todoPage = new ToDoPage(driver);
    }

    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                arguments("Backbone.js", List.of("Buy sugar", "Buy water"), List.of("Buy sugar"), 1),
                arguments("Backbone.js", List.of("Buy sugar", "Buy water"), List.of("Buy sugar"), 2),
                arguments("React New", List.of("Buy sugar", "Buy water"), List.of("Buy sugar"), 2),
                arguments("React New", List.of("Buy milk", "Buy bread"), List.of("Buy bread", "Buy milk"), 0)
        );
    }

    @ParameterizedTest(name = "Test {index} - {0}")
    @MethodSource("provideParameters")
    @Link(name = "Website", url = "https://todomvc.com/")
    @Owner("User")
    @Description("Questo test prova a creare degli item in una ToDo list, spuntarne alcuni e assicurarsi che siano stati creati con successo")
    @Epic("TodoMVC")
    @Feature("ToDo List")
    @Story("Utente crea e spunta task in una lista")
    @Severity(SeverityLevel.CRITICAL)
    public void toDoListTest(
            String technology,
            List<String> todoItems,
            List<String> todoItemsToCheck,
            Integer expectedLeftItems
    ) throws InterruptedException {
        driver.navigate().to("https://todomvc.com/");

        Allure.parameter("technology", technology);
        Allure.parameter("todoItems", todoItems);
        Allure.parameter("todoItemsToCheck", todoItemsToCheck);
        Allure.parameter("expectedLeftItems", expectedLeftItems);

        Allure.step("click on " + technology);
        mainPage.clickLink(technology);

        Allure.step("Aggiungo delle task alla lista", step -> {
            for (String todoItem : todoItems) {
                Allure.step("Aggiungo l'item " + todoItem);
                todoPage.addItem("todoItem");
            }
        });


        Allure.step("Spunto delle task nella lista", step -> {

            for (String todoItemToCheck : todoItemsToCheck) {
                Allure.step("Spunto l'item " + todoItemToCheck);
                todoPage.clickCheckbox(todoItemToCheck);
            }
        });

        Allure.step("Verificare il numero di item rimasti");


        todoPage.assertItemsCount(expectedLeftItems);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
