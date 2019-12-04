import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Konica_QA {


    public WebDriver driver;
    String searchBox = "searchInput";
    String resultsLocator = "#typeahead-suggestions p";
    WebDriverWait wait;

    ExtentReports report;
    ExtentHtmlReporter htmlReporter;
    ExtentTest extentLogger;


    @BeforeMethod
    public void setup(){
        // set up test variable
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 5);

        // set up reporter
        report = new ExtentReports();
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/report.html");
        report.attachReporter(htmlReporter);

        htmlReporter.config().setReportName("Simple Wikipedia search demo");
        report.setSystemInfo("OS", System.getProperty("os.name"));
        report.setSystemInfo("Browser", "chrome");

    }

    @Test
    public void test() throws InterruptedException {
        extentLogger = report.createTest("Wikipedia search test");
        extentLogger.info("go to url");

        driver.get("https://www.wikipedia.org/");
        extentLogger.info("enter search term");
        driver.findElement(By.id(searchBox)).sendKeys("konica minolta");
        extentLogger.info("wait for suggestions to load");
        List<WebElement> allResults = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(resultsLocator)));
        extentLogger.info("number of suggestions = " + allResults.size());
        WebElement firstResult = allResults.get(0);
        extentLogger.info("Verifying first suggestion:" + firstResult.getText());
        Assert.assertEquals(firstResult.getText(), "Japanese technology company");
        Assert.assertFalse(firstResult.getText().isEmpty());
        extentLogger.pass("PASSED");
    }

    @AfterMethod
    public void teardown(){
        driver.quit();
        report.flush();
    }



}
