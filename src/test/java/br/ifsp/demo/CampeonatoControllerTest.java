package br.ifsp.demo;

import br.ifsp.demo.DTOs.CampeonatoRequestDTO;
import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import br.ifsp.demo.security.user.User;
import com.github.javafaker.Faker;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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

}
