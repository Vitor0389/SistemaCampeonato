package br.ifsp.demo;

import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.controller.TeamController;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TeamControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TeamRepository repository;
    @Autowired private TeamController controller;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return all saved teams")
    void shouldReturnAllSavedTeams() throws Exception {
        mockMvc.perform(get("/api/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(35)));
    }

    //TODO: CRIAR ISSUE DESSE TESTE, POST PEDE LISTA AO INVÉS DE UM ELEMENTO
    @Test
    @DisplayName("Should create a new team")
    void shouldCreateNewTeam() throws Exception {
        TeamDTO teamDTO = new TeamDTO(UUID.randomUUID(), "Paysandu");

        mockMvc.perform(post("/api/v1/teams")
                        .content(objectMapper.writeValueAsString(teamDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[36].name", is("Paysandu")));;

        List<Team> teams = repository.findAll();
        assertThat(teams).hasSize(36);
        assertThat(teams.get(36).getName()).isEqualTo("Paysandu");
    }

    @Test
    @DisplayName("Should delete all teams")
    void shouldDeleteAllTeams() throws Exception {
        mockMvc.perform(delete("/api/v1/teams"))
                .andExpect(status().isNoContent());

        assertThat(repository.findAll()).isEmpty();
    }
}
