package br.ifsp.demo;

import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.controller.TeamController;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static io.restassured.RestAssured.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TeamControllerTest extends BaseApiIntegrationTest{

    @Autowired private TeamRepository teamRepository;
    @Autowired private TeamController controller;

    @Test
    @DisplayName("Should return all saved teams")
    @Tag("IntegrationTest")
    void shouldReturnAllSavedTeams() {
        List<Team> responseTeams = given().contentType("application/json").port(port)
                .when().get("api/v1/teams")
                .then().statusCode(200)
                .extract().body().jsonPath().getList(".", Team.class);

        final List<UUID> responseIds = responseTeams.stream().map(Team::getId).toList();
        List<Team> databaseTeams = teamRepository.findAll();
        List<UUID> databaseIds = databaseTeams.stream().map(Team::getId).toList();

        assertThat(responseIds).hasSize(databaseIds.size()).containsExactlyInAnyOrderElementsOf(databaseIds);
    }

    @Test
    @DisplayName("Should create a new team")
    @Tag("IntegrationTest")
    void shouldCreateNewTeam() {
        TeamDTO newTeamDTO = new TeamDTO(UUID.randomUUID(), "Paysandu");
        List<TeamDTO> teamDTOs = List.of(newTeamDTO);

        given().contentType("application/json").port(port).body(teamDTOs)
                .when().post("/api/v1/teams")
                .then().statusCode(201)
                .contentType(ContentType.JSON)
                .extract().body().jsonPath().getList(".", TeamDTO.class);
    }

    @Test
    @DisplayName("Should delete all teams")
    @Tag("IntegrationTest")
    void shouldDeleteAllTeams() {
        given().contentType(ContentType.JSON)
                .when().delete("api/v1/teams")
                .then().statusCode(204);
    }
}
