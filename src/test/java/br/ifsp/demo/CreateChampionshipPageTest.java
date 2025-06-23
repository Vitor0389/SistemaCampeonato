package br.ifsp.demo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.text.html.ParagraphView;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateChampionshipPageTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Should create championship with four teams")
    @Tag("UiTest")
    void shouldCreateChampionshipWith4Teams() {
        LoginPageObject loginPage = new LoginPageObject(driver);
        loginPage.navigateTo();
        HomePageObject homePage = loginPage.login("abc@email.com", "123");
        delay(1000);

        assertThat(homePage.verifyActualPage()).isTrue();

        CreateChampionshipPageObject createPage = homePage.clickCreateChampionshipBtn();
        delay(1000);

        assertThat(createPage.verifyActualPage()).isTrue();

        String championshipName = "Copa dos Campeões Europeus";
        List<String> teams = List.of(
                "Manchester United",
                "Real Madrid",
                "Juventus",
                "Bayern Munich"
        );
        delay(1000);

        createPage.writeChampionshipName(championshipName);
        createPage.selectTeams(teams);
        delay(1000);

        createPage.clickCreateBtn();
        delay(1000);
    }

    @Test
    @DisplayName("Should not create a championship with an odd number of teams")
    @Tag("UiTest")
    void shouldNotCreateChampionshipWithOddNumberOfTeams() {
        LoginPageObject loginPage = new LoginPageObject(driver);
        loginPage.navigateTo();
        HomePageObject homePage = loginPage.login("abc@email.com", "123");
        delay(1000);

        assertThat(homePage.verifyActualPage()).isTrue();

        CreateChampionshipPageObject createPage = homePage.clickCreateChampionshipBtn();
        delay(1000);

        assertThat(createPage.verifyActualPage()).isTrue();

        String championshipName = "Copa dos Campeões Europeus";
        List<String> teams = List.of(
                "Manchester United",
                "Real Madrid",
                "Juventus"
        );
        delay(1000);

        createPage.writeChampionshipName(championshipName);
        createPage.selectTeams(teams);
        delay(1000);

        createPage.clickCreateBtn();
        delay(1000);

        assertThat(createPage.getErrorMsg()).isEqualTo("Erro ao criar campeonato");
    }


    // ISSUE: cria campeonato com nome com caracteres especiais
    @Test
    @DisplayName("Creating championship with name with special characters")
    @Tag("UiTest")
    void creatingChampionshipWithSpecialCharacters() {
        LoginPageObject loginPage = new LoginPageObject(driver);
        loginPage.navigateTo();

        HomePageObject homePage = loginPage.login("abc@email.com", "123");
        delay(1000);
        assertThat(homePage.verifyActualPage()).isTrue();

        CreateChampionshipPageObject createPage = homePage.clickCreateChampionshipBtn();
        delay(1000);
        assertThat(createPage.verifyActualPage()).isTrue();

        String championshipName = "Champ@?#";
        List<String> teams = List.of(
                "Manchester United",
                "Real Madrid"
        );
        delay(1000);

        createPage.writeChampionshipName(championshipName);
        createPage.selectTeams(teams);
        delay(1000);

        createPage.clickCreateBtn();
        delay(1000);


        WebElement errorMessage = null;
        try {
            errorMessage = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//p[@style='color: red;']")
                    ));
        } catch (TimeoutException ignored) {
        }

        assertThat(errorMessage).isNotNull();

        assert errorMessage != null;
        assertThat(errorMessage.getText()).contains("Erro ao criar campeonato");
    }
}
