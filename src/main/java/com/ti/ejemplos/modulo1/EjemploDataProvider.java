package com.ti.ejemplos.modulo1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EjemploDataProvider {

    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";

    @BeforeTest
    void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(baseURL);
        driver.manage().window().maximize();
    }

    @AfterTest
    void closeBrowser(){
        driver.quit();
    }

    //MxN -> 3x2
    @DataProvider(name = "LoginProvider")
    Object[][] getDataFromDataProvider(){
        return new Object[][]{
                {"admin","G3-ySzY%"},
                {"admin2","G3-ySzY%"},
                {"admin3","G3-ySzY%"}
        };
    }

    @Test(description = "Test Case de varios usuarios", dataProvider = "LoginProvider")
    void loginTest(String userName, String password) throws InterruptedException {
        WebElement txtLoginUserName = driver.findElement(By.id("user_login"));
        txtLoginUserName.clear();
        txtLoginUserName.sendKeys(userName);

        WebElement txtPassword = driver.findElement(By.name("pwd"));
        txtPassword.clear();
        txtPassword.sendKeys(password);

        Assert.assertTrue(txtLoginUserName.getAttribute("value").equals(userName));
        Thread.sleep(1500);
    }

}
