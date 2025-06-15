package br.ifsp.demo;

import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.controller.TeamController;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TeamControllerTest{

    @Autowired private TeamRepository repository;
    @Autowired private TeamController controller;

    @Test
    @DisplayName("Should return all saved teams")
    @Tag("IntegrationTest")
    void shouldReturnAllSavedTeams() {
        List<Team> responseTeams = given().contentType("application/json").port(port)
                .when().get("api/v1/teams").then().statusCode(200)
                .extract().body().jsonPath().getList(".", Team.class);


        final List<UUID> responseIds = responseTeams.stream().map(Team::getId).toList();
        List<Team> databaseTeams = repository.findAll();
        List<UUID> databaseIds = databaseTeams.stream().map(Team::getId).toList();

        assertThat(responseIds).hasSize(databaseIds.size())
                .containsExactlyInAnyOrderElementsOf(databaseIds);
    }

    //TODO: CRIAR ISSUE DESSE TESTE, POST PEDE LISTA AO INVÉS DE UM ELEMENTO
//    @Test
//    @DisplayName("Should create a new team")
//    @Tag("IntegrationTest")
//    void shouldCreateNewTeam() throws Exception {
//        TeamDTO teamDTO = new TeamDTO(UUID.randomUUID(), "Paysandu");
//
//        mockMvc.perform(post("/api/v1/teams")
//                        .content(objectMapper.writeValueAsString(teamDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$[36].name", is("Paysandu")));;
//
//        List<Team> teams = repository.findAll();
//        assertThat(teams).hasSize(36);
//        assertThat(teams.get(36).getName()).isEqualTo("Paysandu");
//    }

//    @Test
//    @DisplayName("Should delete all teams")
//    @Tag("IntegrationTest")
//    void shouldDeleteAllTeams() throws Exception {
//        mockMvc.perform(delete("/api/v1/teams"))
//                .andExpect(status().isNoContent());
//
//        assertThat(repository.findAll()).isEmpty();
//    }
}
