package br.ifsp.demo;

import br.ifsp.demo.controller.TeamController;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

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
}
