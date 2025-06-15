package br.ifsp.demo;

import br.ifsp.demo.controller.TeamController;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest
public class TeamControllerTest {

    @Autowired TeamRepository repository;
    @Autowired TeamController controller;

    @BeforeEach
    public void setUp() {
        Team team1 = new Team(UUID.randomUUID(), "Cortinthians");
        Team team2 = new Team(UUID.randomUUID(), "Flamengo");
        Team team3 = new Team(UUID.randomUUID(), "São Paulo");
    }


    @Test
    @DisplayName("Should get all teams")
    public void getAllTeams() {
        List<Team> teams = repository.findAll();
        assertThat(teams).hasSize(34);
    }
}
