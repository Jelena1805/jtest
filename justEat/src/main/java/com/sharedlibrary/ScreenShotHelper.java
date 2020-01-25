package com.sharedlibrary;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.ByteArrayInputStream;

/**
 * Screen shot helper
 */
public class ScreenShotHelper {

    public static void takeScreenshot(WebDriver driver) {

        Allure.addAttachment("URL: ", driver.getCurrentUrl());
        try {
            Thread.sleep(1000);
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(screenshot));
            LoggingUtils.log(screenshot, "Screenshot from URL: <" + driver.getCurrentUrl() + ">");
        } catch (Exception e) {
            Assert.fail("Could not take screenshot");
        }
    }

    public static void takeScreenshot(WebDriver driver, String description) {

       //String pageHeight = WebDriverEnMethods.getPageHeight(driver);
       //String pageWidth = WebDriverEnMethods.getPageWidth(driver);

//        Dimension initial_size = driver.manage().window().getSize();
//        int height = initial_size.getHeight();
//        int width = initial_size.getHeight();

       //driver.manage().window().setSize(new Dimension(Integer.parseInt(Integer.toString(width)), Integer.parseInt(Integer.toString(height))));

        Allure.addAttachment("URL: ", driver.getCurrentUrl());
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
            Thread.sleep(1000);
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(description, new ByteArrayInputStream(screenshot));
            LoggingUtils.log(screenshot, "Description: " + description + ", Screenshot from URL: <" + driver.getCurrentUrl() + ">");
        } catch (Exception e) {
            Assert.fail("Could not take screenshot Details");
        }
    }

    public static void addDescription(String description) {

        Allure.addAttachment("- ", description);
    }
}


