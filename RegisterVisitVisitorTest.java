/**
 * This class has test cases for following story:
 * STORY W06. ADD CONTACT
 *
 * @author  Kevin Coorens
 * @version 1.0
 * @since   2020-12-20
 */
import domain.Service.ContactTracingService;
import domain.model.Visit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import pageObjects.LoginPage;
import pageObjects.VisitorPage;
import util.DbConnectionService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class RegisterVisitVisitorTest {

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
        for (Visit visit : service.getVisits()) {
            if (visit.getPerson().getFirstName().equals("Balthazar") &&
                    visit.getPerson().getLastName().equals("Boma") &&
                    visit.getPerson().getPhonenumber().equals("+32696969696") &&
                    visit.getPerson().getEmail().equals("pdg@vleesindustrie.nv")) {
                service.deleteVisit(Integer.toString(visit.getVisitID()));
            }
        }
    }

    @Test
    public void test_Register_Visit_Visitor_AllFieldsCorrect() {
        VisitorPage visitorPage = PageFactory.initElements(driver, VisitorPage.class);
        visitorPage.fillOutForm("Balthazar", "Boma", "pdg@vleesindustrie.nv", "+32696969696");

        LoginPage loginPage = visitorPage.submitValid();

        assertTrue(loginPage.isTitleCorrect());
        assertTrue(loginPage.hasSuccessMessage("Visit successfully registered!"));

        boolean found = false;
        for (Visit visit : service.getVisits()) {
            if (visit.getPerson().getFirstName().equals("Balthazar") &&
                    visit.getPerson().getLastName().equals("Boma") &&
                    visit.getPerson().getPhonenumber().equals("+32696969696") &&
                    visit.getPerson().getEmail().equals("pdg@vleesindustrie.nv")) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void test_Register_Visit_Visitor_NoFirstName() {
        VisitorPage visitorPage = PageFactory.initElements(driver, VisitorPage.class);
        visitorPage.fillOutForm("", "Boma", "pdg@vleesindustrie.nv", "+32696969696");

        visitorPage.submitInvalid();

        assertTrue(visitorPage.isTitleCorrect());
        assertTrue(visitorPage.hasErrorMessage("No first name given"));
        assertTrue(visitorPage.hasEmptyFirstName());
        assertTrue(visitorPage.hasStickyLastName("Boma"));
        assertTrue(visitorPage.hasStickyEmail("pdg@vleesindustrie.nv"));
        assertTrue(visitorPage.hasStickyPhone("+32696969696"));
    }

    @Test
    public void test_Register_Visit_Visitor_NoLastName() {
        VisitorPage visitorPage = PageFactory.initElements(driver, VisitorPage.class);
        visitorPage.fillOutForm("Balthazar", "", "pdg@vleesindustrie.nv", "+32696969696");

        visitorPage.submitInvalid();

        assertTrue(visitorPage.isTitleCorrect());
        assertTrue(visitorPage.hasErrorMessage("No last name given"));
        assertTrue(visitorPage.hasEmptyLastName());
        assertTrue(visitorPage.hasStickyFirstName("Balthazar"));
        assertTrue(visitorPage.hasStickyEmail("pdg@vleesindustrie.nv"));
        assertTrue(visitorPage.hasStickyPhone("+32696969696"));
    }

    @Test
    public void test_Register_Visit_Visitor_NoEmail() {
        VisitorPage visitorPage = PageFactory.initElements(driver, VisitorPage.class);
        visitorPage.fillOutForm("Balthazar", "Boma", "", "+32696969696");

        visitorPage.submitInvalid();

        assertTrue(visitorPage.isTitleCorrect());
        assertTrue(visitorPage.hasErrorMessage("No email given"));
        assertTrue(visitorPage.hasEmptyEmail());
        assertTrue(visitorPage.hasStickyFirstName("Balthazar"));
        assertTrue(visitorPage.hasStickyLastName("Boma"));
        assertTrue(visitorPage.hasStickyPhone("+32696969696"));
    }

    @Test
    public void test_Register_Visit_Visitor_EmailInvalid() {
        VisitorPage visitorPage = PageFactory.initElements(driver, VisitorPage.class);
        visitorPage.fillOutForm("Balthazar", "Boma", "pdg", "+32696969696");

        visitorPage.submitInvalid();

        assertTrue(visitorPage.isTitleCorrect());
        assertTrue(visitorPage.hasErrorMessage("Email not valid"));
        assertTrue(visitorPage.hasEmptyEmail());
        assertTrue(visitorPage.hasStickyFirstName("Balthazar"));
        assertTrue(visitorPage.hasStickyLastName("Boma"));
        assertTrue(visitorPage.hasStickyPhone("+32696969696"));
    }

    @Test
    public void test_Register_Visit_Visitor_NoPhoneNumber() {
        VisitorPage visitorPage = PageFactory.initElements(driver, VisitorPage.class);
        visitorPage.fillOutForm("Balthazar", "Boma", "pdg@vleesindustrie.nv", "");

        visitorPage.submitInvalid();

        assertTrue(visitorPage.isTitleCorrect());
        assertTrue(visitorPage.hasErrorMessage("No phone number given"));
        assertTrue(visitorPage.hasEmptyPhone());
        assertTrue(visitorPage.hasStickyFirstName("Balthazar"));
        assertTrue(visitorPage.hasStickyLastName("Boma"));
        assertTrue(visitorPage.hasStickyEmail("pdg@vleesindustrie.nv"));
    }

    @Test
    public void test_Register_Visit_Visitor_PhoneNumberInvalid() {
        VisitorPage visitorPage = PageFactory.initElements(driver, VisitorPage.class);
        visitorPage.fillOutForm("Balthazar", "Boma", "pdg@vleesindustrie.nv", "1234");

        visitorPage.submitInvalid();

        assertTrue(visitorPage.isTitleCorrect());
        assertTrue(visitorPage.hasErrorMessage("Phone number not valid"));
        assertTrue(visitorPage.hasEmptyPhone());
        assertTrue(visitorPage.hasStickyFirstName("Balthazar"));
        assertTrue(visitorPage.hasStickyLastName("Boma"));
        assertTrue(visitorPage.hasStickyEmail("pdg@vleesindustrie.nv"));
    }
}
