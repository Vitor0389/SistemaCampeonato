package br.ifsp.demo;

import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.controller.TeamController;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.*;



public class TeamControllerTest extends BaseApiIntegrationTest{

    @Autowired private TeamRepository teamRepository;

    private List<Team> teamsBackup;

    @BeforeEach
    public void setUp() {
        teamsBackup = teamRepository.findAll();
    }

    @AfterEach
    public void tearDown() {
        teamRepository.saveAll(teamsBackup);
    }

    @Test
    @DisplayName("Should return all saved teams")
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    void shouldReturnAllSavedTeams() {
        List<Team> responseTeams = given().contentType("application/json")
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
    @Tag("ApiTest")
    void shouldCreateNewTeam() {
        TeamDTO newTeamDTO = new TeamDTO(UUID.randomUUID(), "Paysandu");
        List<TeamDTO> teamDTOs = List.of(newTeamDTO);

        given().contentType("application/json").body(teamDTOs)
                .when().post("/api/v1/teams")
                .then().statusCode(201)
                .contentType(ContentType.JSON)
                .extract().body().jsonPath().getList(".", TeamDTO.class);
    }

    @Test
    @DisplayName("Should delete all teams")
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    void shouldDeleteAllTeams() {
        given().contentType(ContentType.JSON)
                .when().delete("api/v1/teams")
                .then().statusCode(204);
    }

    @Test
    @DisplayName("Should return empty list when repository is empty")
    @Tag("IngrationTest")
    @Tag("ApiTest")
    void shouldReturnEmptyListWhenRepositoryIsEmpty() {
        teamRepository.deleteAll();
        List<Team> responseTeams = given().contentType("application/json")
                .when().get("api/v1/teams")
                .then().statusCode(200)
                .extract().body().jsonPath().getList(".", Team.class);

        assertThat(responseTeams).isEmpty();
    }

    // ISSUE: erro 401 ao invés de 400
    @Test
    @DisplayName("Should not create a team with empty name")
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    void shouldNotCreateTeamWithEmptyName() {
        UUID teamId = UUID.randomUUID();
        TeamDTO newTeamDTO = new TeamDTO(teamId, "");
        List<TeamDTO> teamDTOs = List.of(newTeamDTO);

        given().contentType("application/json").body(teamDTOs)
                .when().post("/api/v1/teams")
                .then().statusCode(400);
    }

    // ISSUE: erro 401 ao invés de 400
    @Test
    @DisplayName("Should not create a team with null id")
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    void shouldNotCreateTeamWithNullId() {
        UUID teamId = UUID.randomUUID();
        TeamDTO newTeamDTO = new TeamDTO(null, "team");
        List<TeamDTO> teamDTOs = List.of(newTeamDTO);

        given().contentType("application/json").body(teamDTOs)
                .when().post("/api/v1/teams")
                .then().statusCode(400);
    }
    @Test
    @DisplayName("Should not create a team with null name")
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    void shouldReturnBadRequestWhenNameIsNull() {
        TeamDTO team = new TeamDTO(UUID.randomUUID(), null);

        given().contentType(ContentType.JSON).body(List.of(team))
                .when().post("/api/v1/teams").then().statusCode(400);
    }
    @Test
    @DisplayName("Should not create a team when sending empty list")
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    void shouldReturnBadRequestWhenEmptyListIsSent() {
        given().contentType(ContentType.JSON).port(port).body(List.of())
                .when().post("/api/v1/teams").then().statusCode(400);
    }
    @Test
    @DisplayName("Should not create duplicated team")
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    void shouldNotCreateDuplicatedTeam() {
        UUID duplicatedId = UUID.randomUUID();
        TeamDTO team1 = new TeamDTO(duplicatedId, "Team Duplicated");
        TeamDTO team2 = new TeamDTO(duplicatedId, "Team Duplicated");

        given().contentType("application/json").port(port).body(List.of(team1, team2))
                .when().post("/api/v1/teams").then().statusCode(400);
    }

}
