package br.ifsp.demo;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginPageObject extends BasePageObject {
    private static final String PAGE_URL = "http://localhost:3000/login";

    @FindBy(xpath = "//input[@placeholder='E-mail']")
    private WebElement inputEmail;

    @FindBy(xpath = "//input[@placeholder='Senha']")
    private WebElement inputPassword;

    @FindBy(xpath = "//button[@type='submit' and text()='Entrar']")
    private WebElement buttonEnter;

    public LoginPageObject(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateToURL(PAGE_URL);
    }

    public HomePageObject login(String email, String senha) {
        inputEmail.sendKeys(email);
        inputPassword.sendKeys(senha);
        buttonEnter.click();

        final Alert alert = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.alertIsPresent());
        alert.accept();

        return new HomePageObject(driver);
    }
}