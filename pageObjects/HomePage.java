package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends Page {

    @FindBy(id="logOut")
    private WebElement logOutButton;

    @FindBy(id="visitButton")
    private  WebElement visitButton;

    public HomePage (WebDriver driver) {
        super(driver);
        this.driver.get(path+"?command=Home");
    }

    @Override
    public boolean isTitleCorrect() {
        return this.getTitle().equals("ContactTracing De Treffers Overijse | Home");
    }

    public boolean correctVisitButton(String msg) { return visitButton.getText().equals(msg);}

    public void pressVisitButton() { visitButton.click(); }

    public void changeButtonID(String id) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript
                ("document.getElementById('visitButton').setAttribute('href', 'Controller?command=Visit&action=arrived&id="+ id +"')");
    }

    public void logOut() {
        logOutButton.click();
    }

    public boolean userGetsWelcomeMessage (String user) {
        return findObject("h1", "Welcome " + user);
    }

    public boolean userGetsCovidPositiveWidget() {
        return findObject("h5", "Covid Confirmed!") &&
                findObject("p", "You have submitted a positive covid test.");
    }

    public boolean userGetsNoCovidWidget() {
        return findObject("h5", "No Risk!") &&
                findObject("p", "You haven't had any covid confirmed encounters as of now.");
    }

    public boolean userGetsCovidRiskWidget(int risks) {
        return findObject("h5", "Some Risk!") &&
                findObject("p", "You have had " + risks + " encounter(s) with covid confirmed players.");
    }
}
