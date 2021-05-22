package com.ti.ejemplos.modulo6;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class CrossBrowser {
    WebDriver driver;

    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";

    @BeforeTest
    @Parameters("browser")
    void setUp(String browser){
        switch (browser.toLowerCase()){
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "brave":
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/main/resources/chromedriver.exe");
                ChromeOptions options = new ChromeOptions().setBinary("C:/Program Files/BraveSoftware/Brave-Browser/Application/brave.exe");
                driver = new ChromeDriver(options);
                break;
            default:
                System.err.println("Incorrect Browser");
        }
        driver.get(baseURL);
        driver.manage().window().maximize();
    }

    @BeforeMethod
    void checkDemoSiteHome(){
        Assert.assertTrue(driver.getTitle().contains("Log In"));
    }

    @Test
    void test1(){
        driver.findElement(By.linkText("Register")).click();
        Assert.assertTrue(driver.getTitle().contains("Registration Form"));
    }

    @Test
    void test2(){
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

