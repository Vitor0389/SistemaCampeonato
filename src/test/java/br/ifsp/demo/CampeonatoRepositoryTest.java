package br.ifsp.demo;

import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.CampeonatoRepository;
import br.ifsp.demo.repository.TeamRepository;
import br.ifsp.demo.security.user.JpaUserRepository;
import br.ifsp.demo.security.user.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampeonatoRepositoryTest {

    @Autowired CampeonatoRepository campRepository;
    @Autowired JpaUserRepository userRepository;
    @Autowired TeamRepository teamRepository;

    private User testUser;
    private User anotherUser;

    @BeforeEach
    void setup() {
        tearDown();
        testUser = User.builder().id(randomUUID()).name("Test").lastname("User").email("test.user@" + randomUUID() + ".com").password("password123").build();
        userRepository.save(testUser);

        anotherUser = User.builder().id(randomUUID()).name("Another").lastname("User").email("another.user@" + randomUUID() + ".com").password("anotherpass").build();
        userRepository.save(anotherUser);
    }

    @AfterEach
    void tearDown() {
        campRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    private List<Team> createAndPersistTeams(String... nomes) {
        List<Team> teams = Arrays.stream(nomes)
                .map(n -> new Team(UUID.randomUUID(), n)).toList();
        return teamRepository.saveAll(teams);
    }

}
