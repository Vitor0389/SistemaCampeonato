package br.ifsp.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class DeleteChampionshipPageObject extends BasePageObject{

    private static final String PAGE_URL = "http://localhost:3000/deletar";

    @FindBy(xpath = "//h2[text()='Deletar Campeonatos']")
    private WebElement pageTitle;

    @FindBy(xpath = "//p[@style='color: green;']")
    private WebElement successMsg;

    @FindBy(xpath = "//ul/li")
    private List<WebElement> championshipListItems;

    @FindBy(xpath = "//ul/li[1]/button[text()='Deletar']")
    private WebElement firstDeleteButton;

    public DeleteChampionshipPageObject(WebDriver driver) {
        super(driver);
    }

    public boolean verifyActualPage() {
        return super.verifyURL(PAGE_URL);
    }

    public void clickDeleteChampionship(String championshipName) {
        String xpathBtn = String.format("//li[contains(., '%s')]/button[text()='Deletar']", championshipName);
        WebElement deleteBtn = driver.findElement(By.xpath(xpathBtn));
        deleteBtn.click();
    }

    public boolean championshipIsPresent(String championshipName) {
        try {
            String xpathChampionship = String.format("//li[contains(., '%s')]", championshipName);
            return driver.findElement(By.xpath(xpathChampionship)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMsg() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successMsg));
            return successMsg.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void deleteAllChampionships() {
        List<WebElement> currentItems = driver.findElements(By.xpath("//ul/li"));
        while (!currentItems.isEmpty()) {
            currentItems.getFirst().findElement(By.tagName("button")).click();
            wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath("//ul/li"), currentItems.size()));
            currentItems = driver.findElements(By.xpath("//ul/li"));
        }
    }

    public boolean championshipListIsEmpty() {
        return driver.findElements(By.xpath("//ul/li")).isEmpty();
    }
}
