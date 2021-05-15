package com.ti.ejemplos.modulo4;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;


public class EjemploLogs {

    private static Logger log = LogManager.getLogger(EjemploLogs.class.getName());
    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";

    @BeforeTest
    void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(baseURL);
        log.info("Opening Website : " + baseURL);
        driver.manage().window().maximize();
    }

    @BeforeMethod
    void checkDemoSiteHome(){
        log.info("Check if you are in the demo site home page");
        Assert.assertTrue(driver.getTitle().contains("Log In"));
    }

    @Test(priority = 1)
    void testVerifyRegisterPage(){
        driver.findElement(By.linkText("Register")).click();
        log.info("Check if you are on register page");
        Assert.assertTrue(driver.getTitle().contains("Registration Form"));
    }

    @Test(priority = 2)
    void testVerifyLostPassPage(){
        try{
            driver.findElement(By.linkText("Lost your passwo")).click();
            Assert.assertTrue(driver.getTitle().contains("Lost Password"));
        }catch (WebDriverException we){
            log.error(we.getMessage());
        }
    }

    @AfterMethod
    void navigateBack(){
        driver.navigate().back();
        log.info("Back in the navigation history");
    }

    @AfterTest
    void closeBrowser(){
        driver.quit();
        log.info("Browser Closed!");
    }
}
