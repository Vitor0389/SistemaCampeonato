package br.ifsp.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePageObject extends BasePageObject {
    private static final String PAGE_URL = "http://localhost:3000/main";

    @FindBy(xpath = "//h2[text()='Menu Principal']")
    private WebElement mainTitle;

    @FindBy(xpath = "//button[text()='Criar Campeonato']")
    private WebElement createChampionshipBtn;

    @FindBy(xpath = "//button[text()='Deletar Campeonato']")
    private WebElement deleteChampionshipBtn;

    @FindBy(xpath = "//button[text()='Registrar Resultado de Partida']")
    private WebElement registerMatchResultBtn;

    @FindBy(xpath = "//button[contains(@class, 'logout') and text()='Sair']")
    private WebElement exitBtn;

    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    public boolean verifyActualPage() {
        return verifyURL(PAGE_URL);
    }
    
    public CreateChampionshipPageObject clickCreateChampionshipBtn() {
        createChampionshipBtn.click();
        return new CreateChampionshipPageObject(driver);
    }

    public DeleteChampionshipPageObject clickDeleteChampionshipBtn() {
        deleteChampionshipBtn.click();
        return new DeleteChampionshipPageObject(driver);
    }

    public RegisterMatchResultPageObject clickRegisterMatchResultBtn() {
        registerMatchResultBtn.click();
        return new RegisterMatchResultPageObject(driver);
    }

    public LoginPageObject clickExitBtn() {
        exitBtn.click();
        return new LoginPageObject(driver);
    }
}