package com.ti.ejemplos.modulo5;

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

public class EjemploScreenshot {
    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";
    String destination;
    String dateOption = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

    public void getScreenshot(WebElement element, String screenShotName) throws IOException {
        destination = System.getProperty("user.dir") + "/results/screenshots/" + screenShotName+dateOption+".png";
        FileUtils.copyFile((element == null?
                ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE)
                :element.getScreenshotAs(OutputType.FILE)),
                new File(destination));
    }

    @BeforeTest
    void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(baseURL);
        driver.manage().window().maximize();
    }

    @BeforeMethod
    void checkDemoSiteHome(){
        Assert.assertTrue(driver.getTitle().contains("Log In"));
    }

    @Test(priority = 1)
    void testVerifyRegisterPage() throws IOException {
        WebElement lnkRegister = driver.findElement(By.linkText("Register"));
        getScreenshot(lnkRegister,"Selenium4");
        lnkRegister.click();
        Assert.assertTrue(driver.getTitle().contains("Registration Form"));
    }

    @Test(priority = 2)
    void testVerifyLostPassPage() throws IOException {
        try {
            driver.findElement(By.linkText("Lost your passw")).click();
            Assert.assertTrue(driver.getTitle().contains("Lost Password"));
        }catch (WebDriverException we){
            getScreenshot(null, "WebDriverException");
        }
    }

    @AfterMethod
    void navigateBack(){
        driver.navigate().back();
    }

    @AfterTest
    void closeBrowser(){
        driver.quit();
    }
}
