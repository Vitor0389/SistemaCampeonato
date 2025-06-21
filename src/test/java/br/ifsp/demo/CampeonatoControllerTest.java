package br.ifsp.demo;

import br.ifsp.demo.DTOs.CampeonatoRequestDTO;
import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import br.ifsp.demo.security.user.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
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
    @Tag("IntegrationTest")
    @Tag("ApiTest")
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
    @Tag("IntegrationTest")
    @Tag("ApiTest")
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
    @Tag("IntegrationTest")
    @Tag("ApiTest")
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
    @Tag("IntegrationTest")
    @Tag("ApiTest")
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
    @Tag("IntegrationTest")
    @Tag("ApiTest")
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
    @Tag("IntegrationTest")
    @Tag("ApiTest")
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
    @Tag("IntegrationTest")
    @Tag("ApiTest")
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
    @Tag("IntegrationTest")
    @Tag("ApiTest")
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
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should return empty list when no championships are registered")
    void shouldReturnEmptyListWhenNoChampionshipsRegistered() {
        given().header("Authorization", "Bearer " + authToken).when().get("/api/v1/campeonatos")
                .then().statusCode(200).body("$", empty()).log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should return one championship when one is registered")
    void shouldReturnOneChampionshipWhenOneIsRegistered() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato Unico",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid"),
                        new TeamDTO(UUID.fromString("a3333333-3333-3333-3333-333333333333"), "Barcelona"),
                        new TeamDTO(UUID.fromString("a4444444-4444-4444-4444-444444444444"), "Bayern Munich")
                )
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(requestDTO)
                .when().post("/api/v1/campeonatos").then().statusCode(201);

        given().header("Authorization", "Bearer " + authToken).when().get("/api/v1/campeonatos")
                .then().statusCode(200).body("$", hasSize(1)).body("[0].name", equalTo("Campeonato Unico"))
                .log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should return phases of a valid championship")
    void shouldReturnPhasesOfValidChampionship() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato Unico",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid"),
                        new TeamDTO(UUID.fromString("a3333333-3333-3333-3333-333333333333"), "Barcelona"),
                        new TeamDTO(UUID.fromString("a4444444-4444-4444-4444-444444444444"), "Bayern Munich")
                )
        );

        String campeonatoId =
                given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(requestDTO)
                        .when().post("/api/v1/campeonatos").then().statusCode(201).extract().path("id");

        UUID campeonatoidUuid = UUID.fromString(campeonatoId);

        given().header("Authorization", "Bearer " + authToken).when().get("/api/v1/campeonatos/{id}", campeonatoidUuid)
                .then().statusCode(200).body("$", not(empty())).body("[0].name", containsString("Fase"))
                .body("[0].partidas", hasSize(2)).log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should return 404 when championship does not exist")
    void shouldReturnNotFoundWhenChampionshipDoesNotExist() {
        UUID fakeId = UUID.randomUUID();
        given().header("Authorization", "Bearer " + authToken)
                .when().get("/api/v1/campeonatos/{id}", fakeId).then().statusCode(404).log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should register result of a match with valid data")
    void shouldRegisterMatchResultSuccessfully() {
        CampeonatoRequestDTO request = new CampeonatoRequestDTO(
                "Copa Patch",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid")
                )
        );

        String campeonatoIdStr = given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(request).when()
                .post("/api/v1/campeonatos").then().statusCode(201).extract().path("id");

        UUID campeonatoId = UUID.fromString(campeonatoIdStr);

        String partidaIdStr = given().header("Authorization", "Bearer " + authToken).when().get("/api/v1/campeonatos/" + campeonatoId)
                .then().statusCode(200).extract().path("[0].partidas[0].uuid");
        UUID partidaId = UUID.fromString(partidaIdStr);

        TeamDTO vencedor = new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United");

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(vencedor)
                .when().patch("/api/v1/campeonatos/{campId}/resultado/partida/{partidaId}", campeonatoId, partidaId)
                .then().statusCode(204).log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should return 400 when winner does not belong to the match")
    void shouldReturnBadRequestWhenWinnerIsNotInMatch(){
        CampeonatoRequestDTO request = new CampeonatoRequestDTO(
                "Copa Patch",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid")
                )
        );

        String campeonatoIdStr = given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(request).when()
                .post("/api/v1/campeonatos").then().statusCode(201).extract().path("id");

        UUID campeonatoId = UUID.fromString(campeonatoIdStr);

        String partidaIdStr = given().header("Authorization", "Bearer " + authToken).when().get("/api/v1/campeonatos/" + campeonatoId)
                .then().statusCode(200).extract().path("[0].partidas[0].uuid");
        UUID partidaId = UUID.fromString(partidaIdStr);

        TeamDTO teamForaDaPartida  = new TeamDTO(UUID.fromString("a3333333-3333-3333-3333-333333333333"), "Barcelona");

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(teamForaDaPartida)
                .when().patch("/api/v1/campeonatos/{campId}/resultado/partida/{partidaId}", campeonatoId, partidaId)
                .then().statusCode(400).log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should return 404 when trying to delete a non-existing championship")
    void shouldReturnNotFoundWhenDeletingNonExistingChampionship() {
        UUID fakeId = UUID.randomUUID();
        given().header("Authorization", "Bearer " + authToken).when().delete("/api/v1/campeonatos/{id}", fakeId)
                .then().statusCode(404).log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should delete championship successfully")
    void shouldDeleteChampionshipSuccessfully() {
        CampeonatoRequestDTO request = new CampeonatoRequestDTO(
                "Campeonato Deletavel",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid")
                )
        );

        String campeonatoId = given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON)
                .body(request).when().post("/api/v1/campeonatos").then()
                .statusCode(201).extract().path("id");
        UUID campeonatoUuid = UUID.fromString(campeonatoId);
        given().header("Authorization", "Bearer " + authToken).when().delete("/api/v1/campeonatos/{id}", campeonatoUuid)
                .then().statusCode(204);
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should return 400 when teams are duplicated")
    void shouldReturnBadRequestWhenTeamsAreDuplicated() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato com Times Duplicados",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid"),
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(UUID.fromString("a3333333-3333-3333-3333-333333333333"), "Barcelona")
                )
        );

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(requestDTO).when()
                .post("/api/v1/campeonatos").then().statusCode(400).log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should return 400 when a team has a null ID")
    void shouldReturnBadRequestWhenTeamIdIsNull() {
        CampeonatoRequestDTO requestDTO = new CampeonatoRequestDTO(
                "Campeonato com Time com ID Nulo",
                Arrays.asList(
                        new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                        new TeamDTO(null, "Real Madrid")
                )
        );
        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(requestDTO)
                .when().post("/api/v1/campeonatos").then().statusCode(400).log().all();
    }
    @Test
    @Tag("IntegrationTest")
    @Tag("ApiTest")
    @DisplayName("Should create championship with 32 teams successfully")
    void shouldCreateChampionshipWith32TeamsSuccessfully() {
        List<TeamDTO> teams = Arrays.asList(
                new TeamDTO(UUID.fromString("a1111111-1111-1111-1111-111111111111"), "Manchester United"),
                new TeamDTO(UUID.fromString("a2222222-2222-2222-2222-222222222222"), "Real Madrid"),
                new TeamDTO(UUID.fromString("a3333333-3333-3333-3333-333333333333"), "Barcelona"),
                new TeamDTO(UUID.fromString("a4444444-4444-4444-4444-444444444444"), "Bayern Munich"),
                new TeamDTO(UUID.fromString("a5555555-5555-5555-5555-555555555555"), "Liverpool"),
                new TeamDTO(UUID.fromString("a6666666-6666-6666-6666-666666666666"), "Chelsea"),
                new TeamDTO(UUID.fromString("a7777777-7777-7777-7777-777777777777"), "Arsenal"),
                new TeamDTO(UUID.fromString("a8888888-8888-8888-8888-888888888888"), "Juventus"),
                new TeamDTO(UUID.fromString("a9999999-9999-9999-9999-999999999999"), "Paris Saint-Germain"),
                new TeamDTO(UUID.fromString("b1111111-1111-1111-1111-111111111111"), "Manchester City"),
                new TeamDTO(UUID.fromString("b2222222-2222-2222-2222-222222222222"), "AC Milan"),
                new TeamDTO(UUID.fromString("b3333333-3333-3333-3333-333333333333"), "Inter Milan"),
                new TeamDTO(UUID.fromString("b4444444-4444-4444-4444-444444444444"), "Atletico Madrid"),
                new TeamDTO(UUID.fromString("b5555555-5555-5555-5555-555555555555"), "Borussia Dortmund"),
                new TeamDTO(UUID.fromString("b6666666-6666-6666-6666-666666666666"), "Tottenham Hotspur"),
                new TeamDTO(UUID.fromString("b7777777-7777-7777-7777-777777777777"), "Ajax"),
                new TeamDTO(UUID.fromString("b8888888-8888-8888-8888-888888888888"), "Napoli"),
                new TeamDTO(UUID.fromString("b9999999-9999-9999-9999-999999999999"), "RB Leipzig"),
                new TeamDTO(UUID.fromString("c1111111-1111-1111-1111-111111111111"), "Sevilla"),
                new TeamDTO(UUID.fromString("c2222222-2222-2222-2222-222222222222"), "Valencia"),
                new TeamDTO(UUID.fromString("c3333333-3333-3333-3333-333333333333"), "Olympique Lyonnais"),
                new TeamDTO(UUID.fromString("c4444444-4444-4444-4444-444444444444"), "Bayer Leverkusen"),
                new TeamDTO(UUID.fromString("c5555555-5555-5555-5555-555555555555"), "AS Roma"),
                new TeamDTO(UUID.fromString("c6666666-6666-6666-6666-666666666666"), "Lazio"),
                new TeamDTO(UUID.fromString("c7777777-7777-7777-7777-777777777777"), "Monaco"),
                new TeamDTO(UUID.fromString("c8888888-8888-8888-8888-888888888888"), "Porto"),
                new TeamDTO(UUID.fromString("c9999999-9999-9999-9999-999999999999"), "Benfica"),
                new TeamDTO(UUID.fromString("d1111111-1111-1111-1111-111111111111"), "Schalke 04"),
                new TeamDTO(UUID.fromString("d2222222-2222-2222-2222-222222222222"), "Villarreal"),
                new TeamDTO(UUID.fromString("d3333333-3333-3333-3333-333333333333"), "Zenit Saint Petersburg"),
                new TeamDTO(UUID.fromString("d4444444-4444-4444-4444-444444444444"), "Olympique de Marseille"),
                new TeamDTO(UUID.fromString("d5555555-5555-5555-5555-555555555555"), "Celtic")
        );

        CampeonatoRequestDTO request = new CampeonatoRequestDTO("Campeonato Completo", teams);

        given().header("Authorization", "Bearer " + authToken).contentType(ContentType.JSON).body(request)
                .when().post("/api/v1/campeonatos").then().statusCode(201).body("id", notNullValue())
                .body("name", equalTo("Campeonato Completo")).body("teams", hasSize(32)).log().all();
    }

}
