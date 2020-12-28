package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class VisitorPage extends Page {

    @FindBy(id="firstName")
    private WebElement firstNameField;

    @FindBy(id="lastName")
    private WebElement lastNameField;

    @FindBy(id="email")
    private WebElement emailField;

    @FindBy(id="phone")
    private WebElement phoneField;

    @FindBy(id="signUp")
    private WebElement registerVisitButton;

    public VisitorPage(WebDriver driver) {
        super(driver);
        this.driver.get(getPath()+"?command=Visitor");
    }

    @Override
    public boolean isTitleCorrect() {
        System.out.println(this.getTitle());
        return this.getTitle().equals("ContactTracing De Treffers Overijse | Visitor");
    }

    public void setFirstName(String firstName) {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void setLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void setEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void setPhone(String email) {
        phoneField.clear();
        phoneField.sendKeys(email);
    }


    public void fillOutForm(String firstName, String lastName, String email, String phone) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPhone(phone);
    }

    public LoginPage submitValid() {
        registerVisitButton.click();
        //return PageFactory.initElements(driver, LoginPage.class);
        return new LoginPage(driver, true);
    }

    public void submitInvalid() {
        registerVisitButton.click();
    }

    public boolean hasStickyFirstName (String firstname) { return firstname.equals(firstNameField.getAttribute("value")); }

    public boolean hasStickyLastName (String lastname) {
        return lastname.equals(lastNameField.getAttribute("value"));
    }

    public boolean hasStickyEmail (String email) {
        return email.equals(emailField.getAttribute("value"));
    }

    public boolean hasStickyPhone (String phone) {
        return phone.equals(phoneField.getAttribute("value"));
    }

    public boolean hasEmptyFirstName () {
        return firstNameField.getAttribute("value").isEmpty();
    }

    public boolean hasEmptyLastName () {
        return lastNameField.getAttribute("value").isEmpty();
    }

    public boolean hasEmptyEmail () {
        return emailField.getAttribute("value").isEmpty();
    }

    public boolean hasEmptyPhone () {
        return phoneField.getAttribute("value").isEmpty();
    }

}
