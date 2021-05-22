package com.ti.ejemplos.modulo5;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EjemploExtentReport {
    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";
    String destination;
    String dateOption = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    ExtentReports extent;
    ExtentSparkReporter tiSpark;

    public String getScreenshot(WebElement element, String screenShotName) throws IOException {
        destination = System.getProperty("user.dir") + "/results/screenshots/" + screenShotName+dateOption+".png";
        FileUtils.copyFile((element == null?
                        ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE)
                        :element.getScreenshotAs(OutputType.FILE)),
                new File(destination));
        return destination;
    }

    @BeforeTest
    void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(baseURL);
        driver.manage().window().maximize();
        extent = new ExtentReports();
        tiSpark = new ExtentSparkReporter("results/TIReport.html");
        extent.attachReporter(tiSpark);
    }

    @BeforeMethod
    void checkDemoSiteHome() throws IOException {
        Assert.assertTrue(driver.getTitle().contains("Log In"));
        extent.createTest("checkDemoSiteHome")
                .pass(MediaEntityBuilder
                        .createScreenCaptureFromPath(getScreenshot(null,"demosite")).build());
    }

    @Test(priority = 1)
    void testVerifyRegisterPage() throws IOException {
        WebElement lnkRegister = driver.findElement(By.linkText("Register"));
        lnkRegister.click();
        Assert.assertTrue(driver.getTitle().contains("Registration Form"));
        extent.createTest("testVerifyRegisterPage").pass(String.valueOf(Status.PASS));
    }

    @Test(priority = 2)
    void testVerifyLostPassPage() throws IOException {
        try {
            driver.findElement(By.linkText("Lost your passw")).click();
            Assert.assertTrue(driver.getTitle().contains("Lost Password"));
        }catch (WebDriverException we){
            extent.createTest("testVerifyLostPassPage").fail(we.getMessage());
        }
    }

    @AfterMethod
    void navigateBack(){
        driver.navigate().back();
    }

    @AfterTest
    void closeBrowser(){
        driver.quit();
        extent.flush();
    }
}
