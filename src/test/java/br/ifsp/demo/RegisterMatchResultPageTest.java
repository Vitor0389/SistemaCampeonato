package br.ifsp.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterMatchResultPageTest extends BaseSeleniumTest{
    private HomePageObject homePage;

    @BeforeEach
    void navigateToCreateChampionshipPage() {
        LoginPageObject loginPage = new LoginPageObject(driver);
        loginPage.navigateTo();
        this.homePage = loginPage.login("abc@email.com", "123");

        assertThat(homePage.verifyActualPage()).isTrue();
    }

    @Test
    @DisplayName("Should create a championship with two teams and register the result")
    void shouldCreateAChampionshipWithTwoTeamsAndRegisterTheResult() {
        CreateChampionshipPageObject createPage = homePage.clickCreateChampionshipBtn();
        assertTrue(createPage.verifyActualPage());
        delay(1000);

        String championshipName = "Championship Test";
        List<String> times = List.of("Real Madrid", "Barcelona");
        delay(1000);

        createPage.writeChampionshipName(championshipName);
        createPage.selectTeams(times);
        createPage.clickCreateBtn();
        delay(1000);

        driver.navigate().back();

        RegisterMatchResultPageObject registerPage = homePage.clickRegisterMatchResultBtn();
        assertThat(registerPage.verifyActualPage()).isTrue();
        delay(1000);

        registerPage.selectChampionship(championshipName);
        delay(1000);

        registerPage.clickPhase("Fase Inicial");
        delay(1000);

        registerPage.selectWinner("Real Madrid", "Barcelona");
        delay(2000);

        assertThat(registerPage.getWinnerOfMatch("Real Madrid", "Barcelona")).isEqualTo("Vencedor: Real Madrid");
        delay(1000);
    }


}
