package com.ti.ejemplos.modulo2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class EjemploJavaScript {
    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";
    String userName = "admin";
    String password = "G3-ySzY%";
    JavascriptExecutor js;
    String pageLoadStatus = "";

    private void highLight(WebElement element){
        js = (JavascriptExecutor)driver;
        for (byte iCnt=0; iCnt<3; iCnt++){
            try {
                js.executeScript("arguments[0].setAttribute('style','background:red')",element);
                Thread.sleep(500);
                js.executeScript("arguments[0].setAttribute('style','background:')",element);
            }catch (JavascriptException jse){
                System.err.println("Class: EjemploJavaScript | Method: highLight | Exception desc: " + jse.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void scrollWindow(String scroll){
        try {
            js = (JavascriptExecutor)driver;
            switch (scroll){
                case "up":
                    js.executeScript("window.scrollBy(0,-250)");
                    break;
                case "down":
                    js.executeScript("window.scrollBy(0,250)");
                    break;
                default:
                    System.err.println("Bad option");
                    break;
            }
        }catch (JavascriptException jse){
            System.err.println("Class: EjemploJavaScript | Method: scrollWindow | Exception desc: " + jse.getMessage());
        }
    }

    private void waitForPageToLoad(){
        try {
            js = (JavascriptExecutor)driver;
            do{
                pageLoadStatus = (String)js.executeScript("return document.readyState");
            }while (!pageLoadStatus.equals("complete"));
        }catch (JavascriptException jse){
            System.err.println("Class: EjemploJavaScript | Method: waitForPageToLoad | Exception desc: " + jse.getMessage());
        }
    }

    void preLoading(){
        WebElement dvPreLoading = driver.findElement(By.className("wpsp-preLoading"));
        try{
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOf(dvPreLoading));
        }catch (TimeoutException te){
            driver.navigate().refresh();
            preLoading();
        }
    }

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

    @Test
    void testMenuTeacherExist(){
        WebElement txtLoginUserName = driver.findElement(By.id("user_login"));
        highLight(txtLoginUserName);
        txtLoginUserName.clear();
        txtLoginUserName.sendKeys(userName);

        WebElement txtPassword = driver.findElement(By.name("pwd"));
        highLight(txtPassword);
        txtPassword.clear();
        txtPassword.sendKeys(password);

        WebElement btnLogin = driver.findElement(By.xpath("//input[contains(@value,'Log')]"));
        highLight(btnLogin);
        js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();",btnLogin);

        preLoading();

        waitForPageToLoad();

        scrollWindow("down");

        WebElement spnTeacher = driver.findElement(By.xpath("(//span[contains(text(),'Teach')])[1]"));
        highLight(spnTeacher);
        Assert.assertTrue(spnTeacher.getText().contains("Teach"));
    }
}
