package br.ifsp.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class UITests extends BaseSeleniumTest{

    @Test
    @DisplayName("Should open register page")
    @Tag("UiTest")
    public void shouldOpenRegisterPage() {
        driver.get("http://localhost:3000/login");
        final WebElement registerButton =
                driver.findElement(By.xpath("//button[not(@type='submit')]"));
        delay(1000);
        registerButton.click();
        delay(1000);
    }

    @Test
    @DisplayName("Should register account")
    @Tag("UiTest")
    public void shouldRegisterAccount() {
        driver.get("http://localhost:3000/login");
        final WebElement registerButton =
                driver.findElement(By.xpath("//button[not(@type='submit')]"));
        delay(500);
        registerButton.click();
        delay(500);

        final WebElement inputName =
                driver.findElement(By.xpath("//input[@placeholder='Nome']"));
        final WebElement inputSurname =
                driver.findElement(By.xpath("//input[@placeholder='Sobrenome']"));
        final WebElement inputEmail =
                driver.findElement(By.xpath("//input[@placeholder='E-mail']"));
        final WebElement inputPassword =
                driver.findElement(By.xpath("//input[@placeholder='Senha']"));

        inputName.sendKeys("Pedro");
        inputSurname.sendKeys("Candido");
        inputEmail.sendKeys("pedro@email.com");
        inputPassword.sendKeys("123456");
        delay(1000);

        registerButton.click();
        delay(1000);
    }

    @Test
    @DisplayName("Should register account and login")
    @Tag("UiTest")
    public void shouldRegisterAccountAndLogin() {
        driver.get("http://localhost:3000/login");
        final WebElement registerButton =
                driver.findElement(By.xpath("//button[not(@type='submit')]"));
        delay(500);
        registerButton.click();
        delay(500);

        final WebElement inputName =
                driver.findElement(By.xpath("//input[@placeholder='Nome']"));
        final WebElement inputSurname =
                driver.findElement(By.xpath("//input[@placeholder='Sobrenome']"));
        final WebElement inputEmail =
                driver.findElement(By.xpath("//input[@placeholder='E-mail']"));
        final WebElement inputPassword =
                driver.findElement(By.xpath("//input[@placeholder='Senha']"));

        inputName.sendKeys("Pedro");
        inputSurname.sendKeys("Candido");
        inputEmail.sendKeys("pedro@email.com");
        inputPassword.sendKeys("123456");
        delay(1000);
        registerButton.click();
        delay(1000);

        final WebElement loginButton =
                driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();
        delay(2000);
    }
}
