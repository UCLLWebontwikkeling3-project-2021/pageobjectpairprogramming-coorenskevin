package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public abstract class Page {

    WebDriver driver;
    String path = "http://localhost:8080/ProjectWeb3_war_exploded/Controller";

    public Page (WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public abstract boolean isTitleCorrect();

    public String getPath() {
        return path;
    }

    public String getTitle () {
         return driver.getTitle();
    }

    protected boolean findObject(String type, String text) {
        ArrayList<WebElement> listItems=(ArrayList<WebElement>) driver.findElements(By.cssSelector(type));
        boolean found=false;
        for (WebElement listItem:listItems) {
            if (listItem.getText().contains(text)) {
                found=true;
            }
        }
        return found;
    }

    public boolean hasErrorMessage (String message) {
        List<WebElement> errorMsgs = driver.findElements(By.cssSelector("div.alert-danger ul li"));
        for (WebElement errorMsg : errorMsgs) {
            if (message.equals(errorMsg.getText())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWarningMessage (String message) {
        WebElement warningMsg = driver.findElement(By.cssSelector("div.alert-warning div"));
        return (message.equals(warningMsg.getText()));
    }

    public boolean hasSuccessMessage (String message) {
        WebElement successMsg = driver.findElement(By.cssSelector("div.alert-success div"));
        return (message.equals(successMsg.getText()));
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }
}
