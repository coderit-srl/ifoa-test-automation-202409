import Pages.MainPage;
import Pages.ToDoPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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

    @Test
    public void toDoListTest() throws InterruptedException {
        driver.navigate().to("https://todomvc.com/");

        mainPage.clickLink("React New");

        todoPage.addItem("Test");
        todoPage.addItem("Test 2");

        //Thread.sleep(5000);

        todoPage.clickCheckbox("Test 2");

        //Thread.sleep(5000);

        todoPage.assertItemsCount(1);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
