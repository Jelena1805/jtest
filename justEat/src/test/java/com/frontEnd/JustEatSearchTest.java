package com.frontEnd;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.sharedlibrary.Helper;




public class JustEatSearchTest {
	private static WebDriver driver;
	private static Helper helper;
	private static String postcodeArea;
	private static String homePage;
	
	@BeforeMethod
	@Parameters({ "browser" })
	public  void setUp(@Optional("chrome") String browser) throws Exception {
		helper = Helper.getInstance();
		driver = Helper.getDriver(browser);
		driver.manage().window().setSize(new Dimension(1360, 1000));
		System.out.println("Starting test " + new Object() {}.getClass().getEnclosingMethod().getName());
		homePage = helper.getProperty("homeUrl");
		postcodeArea = helper.getProperty("postcodeArea");
		//driver.navigate().to(helper.getProperty("stagingHome"));
		//String user = "erpUser";
		//String password = "erpPassword";
		//String testName = "Starting test " + new Object() {}.getClass().getEnclosingMethod().getName();
		//helper.signInCP(driver, user, password, testName);

	}
	

	@Test
	public void searchForPostcodeAreaUsingEnter() throws InterruptedException {		
	
		driver.navigate().to(homePage); //// navigate to home page
		WebElement skipToMain = driver.findElement(By.id("skipToMain")); //// look just into form
		WebElement searchInput = skipToMain.findElement(By.name("postcode")); ////find search input
		searchInput.clear();
		searchInput.sendKeys(postcodeArea); ///type 
		searchInput.sendKeys(Keys.ENTER); ///click enter	
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement listing = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("c-listing-loader")));	////wait for listing
		List <WebElement> restaurantList = listing.findElements(By.tagName("section"));//// Collect results as list
		int numberOfRestaurants= restaurantList.size();
		Boolean size = numberOfRestaurants > 1;
		Assert.assertEquals(size, true); //// check is there more than one restaurant in list 				
	}	
	
	@Test
	public void searchForPostcodeAreaClickOnSearchButton() throws InterruptedException {		
		driver.navigate().to(homePage); //// navigate to home page
		WebElement skipToMain = driver.findElement(By.id("skipToMain")); //// look just into form
		WebElement searchInput = skipToMain.findElement(By.name("postcode")); ////find search input
		searchInput.clear();
		searchInput.sendKeys(postcodeArea); ///type 	
		skipToMain.findElement(By.tagName("button")).click(); ////click on search button 
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement listing = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("c-listing-loader")));	////wait for listing
		List <WebElement> restaurantList = listing.findElements(By.tagName("section"));//// Collect results as list
		int numberOfRestaurants= restaurantList.size();
		Boolean size = numberOfRestaurants > 1;
		Assert.assertEquals(size, true); //// check is there more than one restaurant in list 				
	}

		
	@AfterMethod
	public  void closeBrowser() throws Exception {
		helper.takeScreenshot(driver, "JustEatSearchTest");
		driver.quit();
	}
}
