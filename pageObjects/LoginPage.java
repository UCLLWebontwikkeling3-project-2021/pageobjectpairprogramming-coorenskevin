package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends Page {

    @FindBy(id="email")
    private WebElement emailField;

    @FindBy(id="password")
    private WebElement passwordField;

    @FindBy(id="signIn")
    private WebElement signInButton;

    public LoginPage(WebDriver driver) {
        super(driver);
        this.driver.get(getPath()+"?command=Home");
    }

    public LoginPage(WebDriver driver, boolean bool) {
        super(driver);
    }

    @Override
    public boolean isTitleCorrect() {
        return this.getTitle().equals("ContactTracing De Treffers Overijse | Login");
    }

    public void setEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void setPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void fillOutForm(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public HomePage submitValid() {
        signInButton.click();
        return PageFactory.initElements(driver, HomePage.class);
    }

    public void submitInvalid() {
        signInButton.click();
    }

    public boolean hasStickyEmail (String email) {
        return email.equals(emailField.getAttribute("value"));
    }

    public boolean hasEmptyEmail () {
        return emailField.getAttribute("value").isEmpty();
    }

    public boolean hasEmptyPassword () {
        return passwordField.getAttribute("value").isEmpty();
    }

}
