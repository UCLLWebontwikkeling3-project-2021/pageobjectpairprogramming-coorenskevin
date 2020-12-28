/**
 * This class has test cases for following story:
 * STORY W06. ADD CONTACT
 *
 * @author  Kevin Coorens
 * @version 1.0
 * @since   2020-12-20
 */
import domain.Service.ContactTracingService;
import static java.time.temporal.ChronoUnit.MINUTES;
import domain.model.Visit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyContactsPage;
import util.DbConnectionService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class RegisterVisitRegisteredUserTest {

    private WebDriver driver;
    private ContactTracingService service;

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Disable javascript (client side form validation)
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.javascript", 2);
        options.setExperimentalOption("prefs", prefs);

        System.setProperty("webdriver.chrome.driver", "/Users/kevincoorens/Servers/chromedriver");
        driver = new ChromeDriver(options);

        // Set up database connection for test(s)
        String dbUrl = "jdbc:postgresql://databanken.ucll.be:62021/2TX34";
        String schema = "web3_project_r0585273";
        DbConnectionService.connect(dbUrl, schema);
        service = new ContactTracingService();
    }

    @After
    public void clean() {
        driver.quit();
        // Clean database
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        for (Visit visit : service.getAllVisitsByUser("5")) {
            if (visit.getDateLD().equals(date) &&
                    MINUTES.between(visit.getArrivalLT(), time) < 1) {
                service.deleteVisit(Integer.toString(visit.getVisitID()));
            }
        }
    }

    @Test
    public void test_Register_Visit_RegisteredUser() {
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.fillOutForm("joskevermeulen@schoten.be", "t");
        HomePage homePage = loginPage.submitValid();
        assertTrue(homePage.isTitleCorrect());
        assertTrue(homePage.correctVisitButton("I have arrived!"));

        // Initiate Visit
        homePage.pressVisitButton();
        assertTrue(homePage.correctVisitButton("I have left!"));
        assertTrue(homePage.hasSuccessMessage("Visit successfully registered for user 'Joske Vermeulen'!"));

        // Check if visit exists in DB
        boolean found = false;
        int visitId = -1;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        for (Visit visit : service.getAllVisitsByUser("5")) {
            if (visit.getDateLD().equals(date) &&
                    MINUTES.between(visit.getArrivalLT(), time) < 1) {
                found = true;
                visitId = visit.getVisitID();
            }
        }
        assertTrue(found);

        // End Visit
        homePage.pressVisitButton();
        assertTrue(homePage.correctVisitButton("I have arrived!"));
        assertTrue(homePage.hasSuccessMessage("Visit successfully terminated for user 'Joske Vermeulen'!"));
        Visit visit = service.getVisit(Integer.toString(visitId));
        assertTrue(MINUTES.between(visit.getDepartureLT(), time) < 1);

        // Check if visit is now displayed on contacts page
        MyContactsPage myContactsPage = PageFactory.initElements(driver, MyContactsPage.class);
        assertTrue(myContactsPage.visitExists(visit.getDate(), visit.getArrival(), visit.getDeparture()));
    }

    @Test
    public void test_Register_Visit_RegisteredUser_tamperWithID() {
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.fillOutForm("joskevermeulen@schoten.be", "t");
        HomePage homePage = loginPage.submitValid();
        assertTrue(homePage.isTitleCorrect());
        assertTrue(homePage.correctVisitButton("I have arrived!"));

        // Try to impersonate someone else
        homePage.changeButtonID("2");
        homePage.pressVisitButton();
        homePage.hasWarningMessage("You have insufficient rights to access the requested page.");
    }

    @Test
    public void test_Register_Visit_RegisteredUser_tamperWithIDAsAdmin() {
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.fillOutForm("admin@ucll.be", "t");
        HomePage homePage = loginPage.submitValid();
        assertTrue(homePage.isTitleCorrect());

        // Try to impersonate someone else (is allowed as an admin)
        homePage.changeButtonID("5");
        homePage.pressVisitButton();
        assertTrue(homePage.hasSuccessMessage("Visit successfully registered for user 'Joske Vermeulen'!"));

        // Check if visit exists in DB
        boolean found = false;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        for (Visit visit : service.getAllVisitsByUser("5")) {
            if (visit.getDateLD().equals(date) &&
                    MINUTES.between(visit.getArrivalLT(), time) < 1) {
                found = true;
            }
        }
        assertTrue(found);
    }
}
