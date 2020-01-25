package com.sharedlibrary;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
//import java.security.DrbgParameters.Capability;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.StandardStream;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import oracle.net.jndi.TrustManager;

public class Helper {

	private static Properties properties;
	private static Helper instance = null;
	private final Logger logger = Logger.getLogger(this.getClass());
	// private Helper helper;

	public Helper() throws IOException {
		URL url;
		File f;

		logger.info("Loading properties file");
		properties = new Properties();
		url = getClass().getClassLoader().getResource("Helper.properties");
		f = new File(url.getPath());
		if (f.exists()) {
			FileReader fr;
			fr = new FileReader(f);
			properties.load(fr);
		}

		// set path for IEDriverServer
		String a = properties.getProperty("webdriver.ie.driver");
		//System.setProperty("webdriver.ie.driver", properties.getProperty("webdriver.ie.driver"));
		// set path for ChromeDriverServer
		String url2 = url.getPath();
		String[] url3 = url2.split("target/");
		String urlChrome = url3[0].toString() + "src/main/resources/drivers/chromedriver.mac";
		String urlFirefox = url3[0].toString() + "src/main/resources/drivers/geckodriver/";
		// String url3 =
		// "/Users/jstanojevic/SeleniumTests/src/main/resources/drivers/chromedriver";
		// String chromedriverpath =
		// this.getClass().getResource("/drivers/chromedriver.mac").getPath().replace("target/classes","src/main/resources");
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver.mac");
		System.setProperty("webdriver.geckodriver.driver", urlFirefox);
	}

	public static Helper getInstance() throws IOException {
		if (instance == null) {
			instance = new Helper();
		}
		return instance;
	}

	/*
	 * get webdriver defined in properties - firefox is the default URL url1 =
	 * getClass().getClassLoader().getResource("NewsGateUnzip/txt.txt");
	 */

	public WebDriver WebDriverChromeWithCap(DesiredCapabilities capabilities) {
		WebDriver driver = null;
		driver = new ChromeDriver(capabilities);
		return driver;
	}

	public static WebDriver getDriver() throws MalformedURLException {

		String webdriver = properties.getProperty("WebDriver");
		if (webdriver.equals("Remote")) {

			System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver.mac");
			String URL = "http://www.google.de";
			String Node = "http://10.131.252.159:4445/wd/hub"; //// moj

			/// String Node = "http://110.131.161.179/wd/hub";
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			WebDriver driver = new RemoteWebDriver(new URL(Node), cap);
			return driver;
		}

		else {
			WebDriver driver = null;
			if (webdriver.equals("Chrome")) {
				driver = new ChromeDriver();
			}
			if (webdriver.equals("Safari")) {
				driver = new SafariDriver();
			}
			if (webdriver.equals("InternetExplorer")) {
				driver = new InternetExplorerDriver();
			}
			if (webdriver.equals("FireFox")) {
				driver = new FirefoxDriver();
			}
			if (driver == null) {
				driver = new FirefoxDriver();
			}
			return driver;
		}

	}

	/**
	 * Return a new RemoteWebDriver instance based on the grid for a given browser
	 * 
	 * @param browser
	 *            a browser that will run the test
	 * @return a new RemoteWebDriver instance
	 * @throws Exception
	 *             if the browser is not mapped
	 */
	public static WebDriver getDriver(String browser) throws Exception {

		// String url = getValueFromConf("grid.url"); //"http://localhost:4444/wd/hub";

		String gridURL = properties.getProperty("grid.url");

		return new RemoteWebDriver(new URL(gridURL), returnCapability(browser));
	}

	/**
	 * Return a DesiredCapability for a given browser
	 * 
	 * @param browser
	 *            the browser name. Allowed browsers are: chrome, firefox & Simulate
	 *            Mobile Devices with Device Mode (chrome).
	 * @return a DesiredCapability
	 * @throws Exception
	 *             if the browser is not mapped
	 */
	private static DesiredCapabilities returnCapability(String browser) throws Exception {
		DesiredCapabilities desiredCapabilities;
		Map<String, Object> chromeOptions;
		String path = "";
		switch (browser.toLowerCase()) {
		case "chrome":
			path = "src/main/resources/chromedriver";
			System.setProperty("webdriver.chrome.driver", path);
			desiredCapabilities = DesiredCapabilities.chrome();
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			desiredCapabilities.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
			desiredCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
					UnexpectedAlertBehaviour.ACCEPT);

			break;

		case "firefox":
			path = "src/main/resources/geckodriver";
			desiredCapabilities = DesiredCapabilities.firefox();
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			desiredCapabilities.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
			desiredCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
					UnexpectedAlertBehaviour.ACCEPT);
			break;

		default:
			throw new Exception("Browser not supported or misspelled");
		}

		return desiredCapabilities;
	}

	/** create driver for beta */

	public WebDriver createDriver(String device, int width, int height, WebDriver driver, String uAgent) {
		driver = null;
		if (device == "Chrome") {
			driver = new ChromeDriver();
		} else if (device == "Firefox") {
			driver = new FirefoxDriver();
		} else if (device == "Safari") {
			driver = new SafariDriver();
		} else {
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("general.useragent.override", uAgent);
			driver = new FirefoxDriver();
			// driver.manage().window().setSize(width, height);
			// driver.manage().window().setSize(new Dimension(width, height));
			// driver.manage().window().setSize(new Dimension(800, 621));
		}
		return driver;
	}

	public static void windowsSwitcher(WebDriver driver) {
		String mainWindow = driver.getWindowHandle();
		Set<String> windowHandels = driver.getWindowHandles();
		String newWindowHandle = "";
		for (String handle : windowHandels) {
			if (!handle.equals(mainWindow)) {
				newWindowHandle = handle;
				break;
			}
			driver.switchTo().window(handle);
		}
		driver.switchTo().window(newWindowHandle);
	}

	public static Boolean Exists(WebElement element) {
		if (element == null) {
			return false;
		}
		return true;
	}

	public WebElement FindElementSafe(WebDriver driver, By by) {
		try {
			return driver.findElement(by);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void addBoarder(WebDriver driver, WebElement element) {
		JavascriptExecutor js;
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'border:3px solid red')", element);
	}

	public void removeBoarder(WebDriver driver, WebElement element) {
		JavascriptExecutor js;
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'border:0 0 0')", element);
	}

	public void focus(WebDriver driver, WebElement element) {
		JavascriptExecutor js;
		js = (JavascriptExecutor) driver;
		String jsToExecute = String.format("var myElements =document.getElementsByClassName('vf-login-button');"
				+ "for (var i = 0; i <myElements.length; i++)" + "{myElements[i].focus();}");
		js.executeScript(jsToExecute);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public String getProperty(String string) {
		return properties.getProperty(string);
	}

	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	public void cancelRedirection(WebDriver driver, String pin) {
		try {
			driver.navigate().to(getProperty("cancel"));
			driver.findElement(By.id("app-input-1")).sendKeys(pin);
			driver.findElement(By.className("btn-normal")).click();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void closeCookieBanner(WebDriver driver) {
		try {
			driver.findElement(By.className("gdpr-cookie-banner__policy-understand")).click();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * public int getId(int type) throws SQLException { String query =
	 * "SELECT  MAX(request_key) AS a FROM pickup_request WHERE REQUEST_TYPE_KEY=" +
	 * type; Connection connection = null; connection = connetToPURSEdb(); Statement
	 * stmt = null; stmt = connection.createStatement(); ResultSet rs =
	 * stmt.executeQuery(query); int id = 0; while (rs.next()) { id =
	 * rs.getInt("a"); } System.out.println("request_key is " + id); return id; }
	 */

	public void DragAndDrop(WebDriver driver, String by) {
		WebElement dragAndDropWraper = driver.findElement(By.cssSelector("app-dropdown[formControlName=province]"));
		dragAndDropWraper.click();
		WebElement ul = driver.findElement(By.cssSelector("ul[aria-labelledby=id]"));
		List<WebElement> dragAndDropList = ul.findElements(By.tagName("li"));
		Object[] dragAndDropArary = dragAndDropList.toArray();
		((WebElement) dragAndDropArary[3]).click();
	}

	public float compareImage(File fileA, File fileB) {

		float percentage = 0;
		try {
			// take buffer data from both image files //
			BufferedImage biA = ImageIO.read(fileA);
			DataBuffer dbA = biA.getData().getDataBuffer();
			int sizeA = dbA.getSize();
			BufferedImage biB = ImageIO.read(fileB);
			DataBuffer dbB = biB.getData().getDataBuffer();
			int sizeB = dbB.getSize();
			int count = 0;
			// compare data-buffer objects //
			if (sizeA == sizeB) {

				for (int i = 0; i < sizeA; i++) {

					if (dbA.getElem(i) == dbB.getElem(i)) {
						count = count + 1;
					}

				}
				percentage = (count * 100) / sizeA;
			} else {
				System.out.println("Both the images are not of same size");
			}

		} catch (Exception e) {
			System.out.println("Failed to compare image files ...");
		}
		return percentage;
	}

	public String GetFileString(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream("src/main/java/jsons/" + fileName);
		String json = IOUtils.toString(fis, "UTF-8");
		return json;
	}

	public void takeScreenshot(WebDriver driver, String testName) throws Exception {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String page1 = "screenShots/" + testName + ".png";
		FileUtils.copyFile(scrFile, new File(page1));
	}

	public Response OkHttpNoSSL(Request request) throws IOException, SQLException {
		Response response;

		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] { new TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}
			} };

			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0]);
			builder.hostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			OkHttpClient client = builder.build();
			response = client.newCall(request).execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

}
