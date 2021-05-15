package com.ti.ejemplos.modulo3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.Duration;

public class EjemploRobot {
    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";
    String userName = "admin";
    String password = "G3-ySzY%";
    String imagePath = "E:\\Dell\\Pictures\\Selenium";
    String imageFile = "Selenium_Logo.jpg";
    Robot robot;

    //Recovery Scenario
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

        WebElement txtLoginUserName = driver.findElement(By.id("user_login"));
        txtLoginUserName.clear();
        txtLoginUserName.sendKeys(userName);

        WebElement txtPassword = driver.findElement(By.name("pwd"));
        txtPassword.clear();
        txtPassword.sendKeys(password);

        driver.findElement(By.xpath("//input[contains(@value,'Log')]")).click();

        preLoading();
    }

    @AfterTest
    void closeBrowser(){
        driver.quit();
    }

    @Test
    void robotExecution() throws AWTException {
        WebElement spnTeacher = new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(driver -> driver.findElement(By.xpath("(//span[contains(text(),'Teachers')])[1]")));
        spnTeacher.click();

        preLoading();

        driver.findElement(By.linkText("Create New")).click();

        preLoading();

       robot = new Robot();

       // Abri la ventana de carga de archivos
       moveAndClick(417,502);

       // Copiar y pegar la ruta
       moveAndClick(378,44);

       selectFromClipboard(imagePath);

       // Clic en el dropodown de extensiones y seleccionar todos los archivos
       moveAndClick(686,413);

       moveAndClick(743,453);

       // Pegar el nombre del archivo y dar clic en enter
       moveAndClick(429,412);

       selectFromClipboard(imageFile);

       robot.keyRelease(KeyEvent.VK_ENTER);
       robot.delay(2000);

    }

    // Método donde agreguemos un string como parámetro y se agregue al clipboard
    private void selectFromClipboard(String elementForClipboard){
        StringSelection stringSelection = new StringSelection(elementForClipboard);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection,null);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
    }

    private void moveAndClick(int x, int y){
        robot.mouseMove(x,y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(1500);
    }

}
