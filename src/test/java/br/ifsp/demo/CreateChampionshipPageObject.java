package br.ifsp.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CreateChampionshipPageObject extends BasePageObject {
    private static final String PAGE_URL = "http://localhost:3000/criar-campeonato";

    @FindBy(xpath = "//h2[text()='Criar Campeonato']")
    private WebElement pageTitle;

    @FindBy(xpath = "//div[label[text()='Nome do Campeonato:']]//input[@type='text']")
    private WebElement inputChampionshipName;

    @FindBy(xpath = "//button[@type='submit' and text()='Criar']")
    private WebElement createBtn;

    @FindBy(xpath = "//p[@style='color: red;']")
    private WebElement errorMsg;

    @FindBy(xpath = "//ul//input[@type='checkbox']")
    private List<WebElement> allTeamCheckboxes;

    public CreateChampionshipPageObject(WebDriver driver) {
        super(driver);
    }

    public boolean verifyActualPage() {
        return super.verifyURL(PAGE_URL);
    }

    public void writeChampionshipName(String nome) {
        inputChampionshipName.sendKeys(nome);
    }

    public void selectTeam(String nomeDoTime) {
        String xpathTime = String.format("//label[contains(., '%s')]/input[@type='checkbox']", nomeDoTime);
        WebElement checkboxTime = driver.findElement(By.xpath(xpathTime));
        checkboxTime.click();
    }

    public void selectTeams(List<String> nomesDosTimes) {
        for (String nome : nomesDosTimes) {
            selectTeam(nome);
        }
    }

    public void selectAllTeams() {
        for (WebElement checkbox : allTeamCheckboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
    }

    public void clickCreateBtn() {
        createBtn.click();
    }

    public String getErrorMsg() {
        return errorMsg.getText();
    }
}