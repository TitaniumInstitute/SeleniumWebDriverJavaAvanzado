package com.ti.ejemplos.modulo1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;

public class MetodoDataProvider {
    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";
    String userName = "admin";
    String password = "G3-ySzY%";

    //Recovery Scenario
    void preLoading(){
        WebElement dvPreLoading = driver.findElement(By.className("wpsp-preLoading"));
        try {
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

        driver.findElement(By.xpath("//input[contains(@value, 'Log')]")).click();

        preLoading();
    }

    @AfterTest
    void closeBrowser(){
        driver.quit();
    }

    @DataProvider(name = "SchoolProvider")
    Object[][] getSchoolData(Method method){
        if(method.getName().equals("testToCreateClass")){
            return new Object[][]{
                    {"Art", "Art002", "20", "Christopher Matte Kenniff","Free"},
                    {"Electroncs", "Ele111", "10", "Holly Canon Woolacott", "Free"}
            };
        }else if (method.getName().equals("testToCreateSubject")){
            return new Object[][]{
                    {"wpsp standard-1", "Math", "Math01", "Wolfie Gallahue", "The Maths Book"},
                    {"wpsp standard-2", "French", "Lang034", "Judye Duhig", "Séak French"},
                    {"wpsp standard-3", "Computer Science", "CS002", "Adam Hodgson", "Introduction to Computr Science"}
            };
        }

        return null;
    }

    @BeforeMethod
    void goToClassesMenu() {
        WebElement mnClasses = new WebDriverWait(driver,Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("(//*[text()='Classes'])[1]"))));
        new Actions(driver).moveToElement(mnClasses).click().build().perform();
        preLoading();
    }

    @Test(description = "Creación de una clase", dataProvider = "SchoolProvider", enabled = true)
    //void testToCreateClass(String name, String number, String capacity, String teacher, String feeType){
    void testToCreateClass(String ... classInfo){
        WebElement lnkClasses = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[text()='Classes'])[2]")));
        lnkClasses.click();

        preLoading();

        WebElement btnCreateNew = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()=' Create New']")));
        btnCreateNew.click();

        preLoading();

        WebElement txtClassName = new WebDriverWait(driver,Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOf(driver.findElement(By.name("Name"))));
        txtClassName.sendKeys(classInfo[0]);//Class Name

        driver.findElement(By.name("Number")).sendKeys(classInfo[1]);//Class Number

        driver.findElement(By.id("c_capacity")).sendKeys(classInfo[2]);//Class Capacity

        new Select(driver.findElement(By.name("ClassTeacherID"))).selectByVisibleText(classInfo[3]); //Teacher

        WebElement dtpStartClass = driver.findElement(By.name("Sdate"));
        new Actions(driver).moveToElement(dtpStartClass).click().build().perform();
        new WebDriverWait(driver,Duration.ofSeconds(5))
                .until(driver -> driver.findElement(By.xpath("//td[contains(@class,'today')]"))).click();

        new Select(driver.findElement(By.name("classfeetype"))).selectByVisibleText(classInfo[4]); //Fee Type (Free)

    }

    @Test(description = "Crear una materia", dataProvider = "SchoolProvider")
    void testToCreateSubject(String className, String subjectName, String subjectCode, String subjectTeacher, String bookName){
        WebElement lnkSubjects = new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Subjects']")));
        lnkSubjects.click();

        preLoading();

        WebElement btnCreateNew = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()=' Create New']")));

        try{
            btnCreateNew.click();
        }catch (ElementClickInterceptedException eci){
            ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,-250)");
            btnCreateNew.click();
        }


        preLoading();

        WebElement drpClass = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(driver -> driver.findElement(By.id("SCID")));
        new Select(drpClass).selectByVisibleText(className);

        driver.findElement(By.name("SNames[]")).sendKeys(subjectName);

        driver.findElement(By.name("SCodes[]")).sendKeys(subjectCode);

        new Select(driver.findElement(By.name("STeacherID[]"))).selectByVisibleText(subjectTeacher);

        driver.findElement(By.name("BNames[]")).sendKeys(bookName);
    }
}
