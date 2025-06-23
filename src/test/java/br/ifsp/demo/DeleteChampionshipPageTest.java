package br.ifsp.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteChampionshipPageTest extends BaseSeleniumTest{
    private HomePageObject homePage;

    @BeforeEach
    void navigateToCreateChampionshipPage() {
        LoginPageObject loginPage = new LoginPageObject(driver);
        loginPage.navigateTo();
        this.homePage = loginPage.login("abc@email.com", "123");

        assertThat(homePage.verifyActualPage()).isTrue();
    }

    @Test
    @DisplayName("Should create and delete a championship")
    void shouldCreateAndThenDeleteAChampionship() {
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

        DeleteChampionshipPageObject deletePage = homePage.clickDeleteChampionshipBtn();
        assertThat(deletePage.verifyActualPage()).isTrue();
        delay(1000);

        assertThat(deletePage.championshipIsPresent(championshipName)).isTrue();

        deletePage.clickDeleteChampionship(championshipName);

        assertThat(deletePage.getSuccessMsg()).isEqualTo("Campeonato deletado com sucesso!");
        assertThat(deletePage.championshipIsPresent(championshipName)).isFalse();
    }

    @Test
    @DisplayName("Should delete all existing championships")
    void shouldDeleteAllExistingChampionships() {
        CreateChampionshipPageObject createPage = homePage.clickCreateChampionshipBtn();
        assertTrue(createPage.verifyActualPage());
        delay(1000);

        createPage.writeChampionshipName("champ1");
        createPage.selectNumberOfTeams(2);
        createPage.clickCreateBtn();
        delay(1000);

        createPage.writeChampionshipName("champ2");
        createPage.selectNumberOfTeams(4);
        createPage.clickCreateBtn();
        delay(1000);

        createPage.writeChampionshipName("champ3");
        createPage.selectNumberOfTeams(8);
        createPage.clickCreateBtn();
        delay(1000);

        driver.navigate().back();

        DeleteChampionshipPageObject deletePage = homePage.clickDeleteChampionshipBtn();
        assertTrue(deletePage.verifyActualPage());

        assertThat(deletePage.championshipListIsEmpty()).isFalse();

        deletePage.deleteAllChampionships();

        assertThat(deletePage.championshipListIsEmpty()).isTrue();
    }
}
