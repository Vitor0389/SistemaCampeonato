package br.ifsp.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class RegisterMatchResultPageObject extends BasePageObject{

    private static final String PAGE_URL = "http://localhost:3000/registrar-resultado";

    @FindBy(xpath = "//h2[text()='Registrar Resultado de Partida']")
    private WebElement pageTitle;

    @FindBy(xpath = "//h3[text()='Selecione o Campeonato']/following-sibling::select")
    private WebElement dropdownChampionship;

    public RegisterMatchResultPageObject(WebDriver driver) {
        super(driver);
    }

    public boolean verifyActualPage() {
        return super.verifyURL(PAGE_URL);
    }

    public void selectChampionship(String championshipName) {
        Select select = new Select(dropdownChampionship);
        select.selectByVisibleText(championshipName);
    }

    public void clickPhase(String phaseName) {
        String xpathPhase = String.format("//button[text()='%s']", phaseName);
        WebElement phaseBtn = driver.findElement(By.xpath(xpathPhase));
        phaseBtn.click();
    }

    public void selectWinner(String winnerTeam, String loserTeam) {
        String xpathMatch = String.format(
                "//li[contains(., '%s') and contains(., '%s')]",
                winnerTeam,
                loserTeam
        );

        WebElement matchItem = driver.findElement(By.xpath(xpathMatch));

        WebElement winnerBtn = matchItem.findElement(By.xpath(String.format(".//button[text()='%s']", winnerTeam)));
        winnerBtn.click();
    }

    public String getWinnerOfMatch(String team1, String team2) {
        try {
            String xpathMatch = String.format("//li[contains(., '%s') and contains(., '%s')]", team1, team2);
            WebElement matchItem = driver.findElement(By.xpath(xpathMatch));
            WebElement winnerStrongTag = matchItem.findElement(By.tagName("strong"));
            wait.until(ExpectedConditions.visibilityOf(winnerStrongTag));
            String fullText = winnerStrongTag.getText();
            return fullText.replace("Vencedor:", "").trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isTeamButtonClickable(String teamToCheck, String otherTeam) {
        try {
            String xpathMatch = String.format(
                    "//li[contains(., '%s') and contains(., '%s')]",
                    teamToCheck,
                    otherTeam
            );
            WebElement matchItem = driver.findElement(By.xpath(xpathMatch));
            WebElement button = matchItem.findElement(By.xpath(String.format(".//button[text()='%s']", teamToCheck)));

            return button.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
}
