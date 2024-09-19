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
    private WebDriverWait webDriverWait;
    private Actions actions;

    @BeforeAll
    public static void setUpClass() {
        //WebDriverManager.chromedriver().setup();
        WebDriverManager.edgedriver().setup();

    }
    
    @BeforeEach
    public void setUp() {
        driver = new EdgeDriver();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
    }

/*    @Test
    public void openGoogleTest() {
        driver.navigate().to("https://www.456bereastreet.com/lab/html5-input-types/");
        WebElement input = waitAndFindElement(By.id("type-text"));

        input.sendKeys("test");
    }
*/

    @Test
    public void toDoListTest() {
        driver.navigate().to("https://todomvc.com/");
        WebElement reactLink = waitAndFindElement(By.linkText("React New"));
        reactLink.click();

        WebElement input = waitAndFindElement(By.id("todo-input"));
        input.sendKeys("test");
        actions.click(input).sendKeys(Keys.ENTER).perform();
        WebElement itemsCount = waitAndFindElement(By.className("todo-count"));
        List<String> items = List.of(itemsCount.getText().split(" "));
        System.out.println(items.get(0));
        Assertions.assertEquals(2, Integer.parseInt(items.get(0)));
    }
    private WebElement waitAndFindElement(By locator) {
        return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
