package br.ifsp.demo;

import br.ifsp.demo.DTOs.CampeonatoRequestDTO;
import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import br.ifsp.demo.security.user.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CampeonatoControllerTest extends BaseApiIntegrationTest{
    @Autowired
    private TeamRepository teamRepository;

    private User testUser;
    private String authToken;
    private List<Team> teamsBackup;

    @BeforeEach
    public void setup() {
        teamsBackup = teamRepository.findAll();
        final String plainTextPassword = "testPassword123";
        testUser = registerUser(plainTextPassword);
        authToken = authenticate(testUser.getEmail(), plainTextPassword);
    }

    @AfterEach
    public void tearDown() {
        teamRepository.saveAll(teamsBackup);
    }

    @Test
    @DisplayName("Should create championship with valid data and authenticated user")
    void shouldCreateChampionshipWithValidDataAndAuthenticatedUser() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Meu Novo Campeonato de Futebol",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid"),
                        new TeamDTO(UUID.fromString("a3333333-3333-3333-3333-333333333333"), "Barcelona"),
                        new TeamDTO(UUID.fromString("a4444444-4444-4444-4444-444444444444"), "Bayern Munich"))
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(requestDTO).when().post("/api/v1/campeonatos")
                .then().statusCode(201).body("id", notNullValue()).body("name", notNullValue()).log().all();
    }

    @Test
    @DisplayName("Should return 400 when championship name is blank")
    void shouldReturnBadRequestWhenNameIsBlank() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid")
                )
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(requestDTO).when().post("/api/v1/campeonatos")
                .then().statusCode(400).log().all();
    }

    @Test
    @DisplayName("Should return 400 when championship name is null")
    void shouldReturnBadRequestWhenNameIsNull() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                null,
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid")
                )
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(requestDTO).when().post("/api/v1/campeonatos")
                .then().statusCode(400).log().all();
    }

    @Test
    @DisplayName("Should return 400 when teams list is insufficient")
    void shouldReturnBadRequestWhenTeamsListIsInsufficient() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato com Poucos Times",
                Collections.singletonList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"))
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(requestDTO).when().post("/api/v1/campeonatos")
                .then().statusCode(400).log().all();
    }

    @Test
    @DisplayName("Should return 400 when teams list is empty")
    void shouldReturnBadRequestWhenTeamsListIsEmpty() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato Sem Times", new ArrayList<>()
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(requestDTO).when().post("/api/v1/campeonatos")
                .then().statusCode(400).log().all();
    }

    @Test
    @DisplayName("Should return 400 when teams list is null")
    void shouldReturnBadRequestWhenTeamsListIsNull() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato com Lista Nula de Times", null
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(requestDTO).when().post("/api/v1/campeonatos")
                .then().statusCode(400).log().all();
    }

    @Test
    @DisplayName("Should create championship with eight teams")
    void shouldCreateChampionshipWithEightTeams() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato de Oito Times",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid"),
                        new TeamDTO(UUID.fromString("a3333333-3333-3333-3333-333333333333"), "Barcelona"),
                        new TeamDTO(UUID.fromString("a4444444-4444-4444-4444-444444444444"), "Bayern Munich"),
                        new TeamDTO(UUID.fromString("a5555555-5555-5555-5555-555555555555"), "Liverpool"),
                        new TeamDTO(UUID.fromString("a6666666-6666-6666-6666-666666666666"), "Chelsea"),
                        new TeamDTO(UUID.fromString("a7777777-7777-7777-7777-777777777777"), "Arsenal"),
                        new TeamDTO(UUID.fromString("a8888888-8888-8888-8888-888888888888"), "Juventus")
                )
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(requestDTO).when().post("/api/v1/campeonatos")
                .then().statusCode(201).body("id", notNullValue()).body("name", notNullValue()).log().all();
    }

    @Test
    @DisplayName("Should not create championship with not power of 2 teams")
    void shouldNotCreateChampionshipWithSixTeams(){
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato de Seis Times",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid"),
                        new TeamDTO(UUID.fromString("a3333333-3333-3333-3333-333333333333"), "Barcelona"),
                        new TeamDTO(UUID.fromString("a4444444-4444-4444-4444-444444444444"), "Bayern Munich"),
                        new TeamDTO(UUID.fromString("a5555555-5555-5555-5555-555555555555"), "Liverpool"),
                        new TeamDTO(UUID.fromString("a6666666-6666-6666-6666-666666666666"), "Chelsea")
                )
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(requestDTO).when().post("/api/v1/campeonatos")
                .then().statusCode(400).log().all();
    }

}
