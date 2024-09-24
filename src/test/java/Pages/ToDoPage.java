package Pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ToDoPage {
    private WebDriver driver;
    private WebDriverWait webDriverWait;
    private Actions actions;

    public ToDoPage(WebDriver driver) {
        this.driver = driver;
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
    }

    public void  addItem(String item) {
        WebElement input = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("todo-input")));
        input.sendKeys(item);
        actions.click(input).sendKeys(Keys.ENTER).perform();
    }

    public void clickCheckbox(String itemText) {
        String xpathLocator = String.format("//label[text()='%s']/preceding-sibling::input", itemText);
        WebElement list = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("todo-list")));
        List<WebElement> items = list.findElements(By.tagName("li"));
        for (WebElement item : items) {
            WebElement itemCheckbox = item.findElement(By.className("toggle"));
            WebElement itemLabel = item.findElement(By.tagName("label"));
            if (itemLabel.getText().equals(itemText)) {
                actions.click(itemCheckbox).perform();
                break;
            }
        }
    }

    public void assertItemsCount(Integer expectedCount) {
        WebElement itemsCount = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("todo-count")));
        List<String> items = List.of(itemsCount.getText().split(" "));
        Assertions.assertEquals(expectedCount, Integer.parseInt(items.get(0)), "Wrong items count");
    }

}
