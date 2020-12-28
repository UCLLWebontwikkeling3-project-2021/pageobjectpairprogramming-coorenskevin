package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

public class MyContactsPage extends Page {

    public MyContactsPage(WebDriver driver) {
        super(driver);
        this.driver.get(getPath()+"?command=Person");
    }

    public void addUserID(String userID) {
        this.driver.get(getPath()+"?command=Person&id="+userID);
    }

    @Override
    public boolean isTitleCorrect() {
        return this.getTitle().equals("ContactTracing De Treffers Overijse | Persons Info");
    }

    public boolean usernameIsCorrect(String username) {
        return findObject("h1", username);
    }

    public boolean visitExists (String date, String arrival, String departure) {
        ArrayList<WebElement> listItems=(ArrayList<WebElement>) driver.findElements(By.cssSelector("tr"));
        boolean found=false;
        for (WebElement listItem:listItems) {
            ArrayList<WebElement> tds = (ArrayList<WebElement>) listItem.findElements(By.cssSelector("td"));
            if(tds.size()>0) {
                if (tds.get(0).getText().equals(date) &&
                        tds.get(1).getText().equals(arrival) &&
                        tds.get(2).getText().equals(departure)) {
                    found = true;
                }
            }
        }
        return found;
    }
}
