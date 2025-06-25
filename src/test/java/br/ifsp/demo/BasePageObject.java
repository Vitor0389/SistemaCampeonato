package br.ifsp.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePageObject {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePageObject(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    protected void navigateToURL(String url) {
        driver.get(url);
    }

    protected boolean verifyURL(String urlEsperada) {
        try {
            wait.until(ExpectedConditions.urlToBe(urlEsperada));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}