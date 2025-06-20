package br.ifsp.demo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
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

        inputName.sendKeys("Matheus");
        inputSurname.sendKeys("Carvalho");
        inputEmail.sendKeys("matheus@email.com");
        inputPassword.sendKeys("abcd");
        delay(1000);

        registerButton.click();
        delay(1000);

        final WebElement loginButton =
                driver.findElement(By.xpath("//button[@type='submit']"));

        loginButton.click();
        delay(3000);
    }

    @Test
    @DisplayName("Should exit main page")
    @Tag("UiTest")
    public void shouldExitMainPage() {
        driver.get("http://localhost:3000/main");
        delay(1000);

        final WebElement exitButton = driver.findElement(By.xpath("//button[not(@class='btn')]"));
        exitButton.click();
        delay(3000);
    }

    @Test
    @DisplayName("Should open create championship page")
    @Tag("UiTest")
    public void shouldOpenCreteChampionshipPage() {
        driver.get("http://localhost:3000/main");
        delay(1000);

        final WebElement createChampionshipBtn =
                driver.findElement(By.xpath("//button[contains(text(), 'Criar Campeonato')]"));
        createChampionshipBtn.click();
        delay(3000);
    }

    @Test
    @DisplayName("Should open create championship page")
    @Tag("UiTest")
    public void shouldOpenFindChampionshipPage() {
        driver.get("http://localhost:3000/main");
        delay(1000);

        final WebElement findChampionshipBtn =
                driver.findElement(By.xpath("//button[contains(text(), 'Buscar Campeonatos')]"));
        findChampionshipBtn.click();
        delay(3000);
    }

    @Test
    @DisplayName("Should open delete championship page")
    @Tag("UiTest")
    public void shouldOpenDeleteChampionshipPage() {
        driver.get("http://localhost:3000/main");
        delay(1000);

        final WebElement deleteChampionshipBtn =
                driver.findElement(By.xpath("//button[contains(text(), 'Deletar Campeonato')]"));
        deleteChampionshipBtn.click();
        delay(3000);
    }

    @Test
    @DisplayName("Should open register match result page")
    @Tag("UiTest")
    public void shouldRegisterMatchResultPage() {
        driver.get("http://localhost:3000/main");
        delay(1000);

        final WebElement registerMatchResultBtn =
                driver.findElement(By.xpath("//button[contains(text(), 'Registrar Resultado de Partida')]"));
        registerMatchResultBtn.click();
        delay(3000);
    }

    @Test
    @DisplayName("Should modify main page window size to test responsiveness")
    @Tag("UiTest")
    public void shouldModifyMainPageWindowSizeToTestResponsiveness() {
        driver.get("http://localhost:3000/main");
        delay(1000);

        driver.manage().window().maximize();
        delay(3000);

        driver.manage().window().fullscreen();
        delay(3000);

        driver.manage().window().minimize();
        delay(3000);

        driver.manage().window().setSize(new Dimension(200, 800));
        delay(3000);
    }
}
