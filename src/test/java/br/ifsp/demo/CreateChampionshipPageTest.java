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

    private CreateChampionshipPageObject createPage;

    @BeforeEach
    void navigateToCreateChampionshipPage() {
        LoginPageObject loginPage = new LoginPageObject(driver);
        loginPage.navigateTo();
        HomePageObject homePage = loginPage.login("abc@email.com", "123");

        assertThat(homePage.verifyActualPage()).isTrue();

        this.createPage = homePage.clickCreateChampionshipBtn();
        assertThat(createPage.verifyActualPage()).isTrue();
    }


    @Test
    @DisplayName("Should create championship with at least two teams")
    @Tag("UiTest")
    void shouldCreateChampionshipWithAtLeastTwoTeams() {
        String championshipName = "2 teams championship";

        List<String> teams = List.of(
                "Manchester United",
                "Real Madrid"
        );

        createPage.writeChampionshipName(championshipName);
        createPage.selectTeams(teams);
        createPage.clickCreateBtn();

        delay(1000);
        assertThat(createPage.getSuccessMsg()).isEqualTo("Campeonato criado com sucesso!");
    }

    @Test
    @DisplayName("Should create championship with 32 teams")
    @Tag("UiTest")
    void shouldCreateChampionshipWithAllTeams() {
        createPage.writeChampionshipName("32 Teams Championship");
        createPage.selectNumberOfTeams(32);
        createPage.clickCreateBtn();

        delay(1000);
        assertThat(createPage.getSuccessMsg()).isEqualTo("Campeonato criado com sucesso!");
    }

    // ISSUE: não cria campeonato com 6 times
    @Test
    @DisplayName("Should create championship with 6 teams")
    @Tag("UiTest")
    void shouldCreateChampionshipWith6Teams() {
        createPage.writeChampionshipName("6 Teams Championship");
        createPage.selectNumberOfTeams(6);
        createPage.clickCreateBtn();
        delay(1000);

        WebElement successMessage = null;
        try {
            successMessage = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//p[@style='color: green;']")
                    ));
        } catch (TimeoutException ignored) {
        }

        assertThat(successMessage).isNotNull();
        assert successMessage != null;
        assertThat(successMessage.getText()).contains("Campeonato criado com sucesso!");
    }

    @Test
    @DisplayName("Should not create a championship with an odd number of teams")
    @Tag("UiTest")
    void shouldNotCreateChampionshipWithOddNumberOfTeams() {
        String championshipName = "EuroChampionship3Teams";
        List<String> teams = List.of(
                "Manchester United",
                "Real Madrid",
                "Juventus"
        );

        createPage.writeChampionshipName(championshipName);
        createPage.selectTeams(teams);
        createPage.clickCreateBtn();

        assertThat(createPage.getErrorMsg()).isEqualTo("Erro ao criar campeonato");
    }

    @Test
    @DisplayName("Should not create a championship with more than 32 teams")
    @Tag("UiTest")
    void shouldNotCreateChampionshipWithMoreThan32Teams() {
        String championshipName = "All Teams Championship";
        createPage.writeChampionshipName(championshipName);
        createPage.selectAllTeams();
        createPage.clickCreateBtn();
        delay(1000);

        assertThat(createPage.getErrorMsg()).isEqualTo("Erro ao criar campeonato");
    }

    @Test
    @DisplayName("Should not create a championship with empty name")
    @Tag("UiTest")
    void shouldNotCreateChampionshipWithEmptyName() {
        List<String> teams = List.of(
                "Manchester United",
                "Real Madrid",
                "Juventus"
        );

        createPage.writeChampionshipName("");
        createPage.selectTeams(teams);
        createPage.clickCreateBtn();

        assertThat(createPage.getErrorMsg()).isEqualTo("Nome do campeonato é obrigatório");
    }

    // ISSUE: cria campeonato com nome com caracteres especiais
    @Test
    @DisplayName("Creating championship with name with special characters")
    @Tag("UiTest")
    void creatingChampionshipWithSpecialCharacters() {
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

    // ISSUE: cria campeonato com nome em branco
    @Test
    @DisplayName("Should not create championship with blank name")
    @Tag("UiTest")
    void shouldNotCreateChampionshipWithBlankName() {
        List<String> teams = List.of(
                "Manchester United",
                "Real Madrid"
        );

        createPage.writeChampionshipName("ㅤㅤ");
        createPage.selectTeams(teams);
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
