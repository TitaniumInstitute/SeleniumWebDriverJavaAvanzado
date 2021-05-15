package com.ti.ejemplos.modulo1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class SeleniumTestNG {
    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";

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

    @Test
    void testVerifyRegisterPage(){
        driver.findElement(By.linkText("Register")).click();
        Assert.assertTrue(driver.getTitle().contains("Registration Form"));
    }

    @Test
    void testVerifyLostPassPage(){
        driver.findElement(By.linkText("Lost your password?")).click();
        Assert.assertTrue(driver.getTitle().contains("Lost Password"));
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
