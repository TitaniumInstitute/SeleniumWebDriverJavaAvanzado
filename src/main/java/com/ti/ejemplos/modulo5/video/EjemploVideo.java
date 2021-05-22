package com.ti.ejemplos.modulo5.video;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class EjemploVideo {
    private ScreenRecorder screenRecorder;
    WebDriver driver;
    String baseURL = "https://demosite.titaniuminstitute.com.mx/wp-admin/admin.php?page=sch-dashboard";

    private void stopRecording() throws IOException {
        this.screenRecorder.stop();
    }

    private void startRecording(String videoPath) throws IOException, AWTException {
        File file = new File(videoPath);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle captureSize = new Rectangle(0,0,screenSize.width, screenSize.height);
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        this.screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
                new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey,MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                        QualityKey, 1.0f, KeyFrameIntervalKey, 15*60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null, file, "ScreenRecorded");
        this.screenRecorder.start();
    }

    @BeforeTest
    void setUp() throws IOException, AWTException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        startRecording(System.getProperty("user.dir")+"/results/video/");
        driver.get(baseURL);
        driver.manage().window().maximize();
    }

    @AfterTest
    void closeBrowser() throws IOException {
        stopRecording();
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
