package generic;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest implements IAutoConst {

	public WebDriver driver;
	public WebDriverWait wait;
	public static ExtentReports report;
	public ExtentTest test;
	
	@BeforeSuite
	public void initReport()
	{
		report=new ExtentReports();
		ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
		report.attachReporter(spark);
	}
	
	@AfterSuite
	public void publishReport() {
		report.flush();
	}
	
	@BeforeMethod
	public void createTest(Method testMethod) {
		String testName=testMethod.getName();
		test = report.createTest(testName);
	}
	
	@Parameters({"config"})
	@BeforeMethod(alwaysRun = true)
	public void openApp(@Optional(CONFIG_PATH) String config) throws MalformedURLException {

		String browser=Utility.getProperty(config,"BROWSER");
		test.info("Browser:"+browser);
		String grid=Utility.getProperty(config, "GRID");
		if(grid.equalsIgnoreCase("Yes"))
		{
			String remote = Utility.getProperty(config, "REMOTE_URL");
			URL url=new URL(remote);
			DesiredCapabilities cap=new DesiredCapabilities();
			cap.setBrowserName(browser);
			driver=new RemoteWebDriver(url,cap);
		}
		else
		{
			if(browser.equalsIgnoreCase("edge"))//switch case
			{
				WebDriverManager.edgedriver().setup();
				driver=new EdgeDriver();
			}
			else if(browser.equalsIgnoreCase("firefox"))
			{
				WebDriverManager.firefoxdriver().setup();
				driver=new FirefoxDriver();
			}
			else
			{
				WebDriverManager.chromedriver().setup();
				driver=new ChromeDriver();
			}
		}
		
		
		driver.manage().window().maximize();
		
		String sITO = Utility.getProperty(config,"ITO");
		long ITO = Long.parseLong(sITO);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ITO));
		
		String sETO = Utility.getProperty(config,"ETO");
		long ETO = Long.parseLong(sETO);
		wait=new WebDriverWait(driver, Duration.ofSeconds(ETO));
		
		String APP_URL = Utility.getProperty(config,"APP_URL");
		driver.get(APP_URL);
	}
	
	@AfterMethod(alwaysRun = true)
	public void closeApp(ITestResult result) throws Exception {
		
		int status=result.getStatus();
		String testName=result.getName();
		if(status==2) 
		{
			TakesScreenshot t=(TakesScreenshot)driver;
			File srcfile = t.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcfile, new File(IMG_PATH+testName+".png")); //add test name with date and time
			//add the screenshot to the test -at the top
//			test.addScreenCaptureFromPath("./../img/test.png");
			
			//add the screenshot to the test - inside the step
			test.fail(MediaEntityBuilder.createScreenCaptureFromPath("./../img/"+testName+".png").build());
			
			String msg=result.getThrowable().getMessage();
			test.fail("Reason:"+msg);  //add screenshot of the application to extent report
			
		}
		
		driver.quit();
	}
	
	@DataProvider
	public Iterator<String[]> getData(Method testMethod) {
		String sheetName=testMethod.getName();
		Iterator<String[]> data = Utility.getDataFromExcel(DATA_PATH,sheetName);
		return data;
	}
}





