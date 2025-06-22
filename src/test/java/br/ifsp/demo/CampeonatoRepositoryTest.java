package br.ifsp.demo;

import br.ifsp.demo.model.Campeonato;
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
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
    @Test
    @DisplayName("findAllByUserId Should Return Only Users Championships")
    void findAllByUserIdShouldReturnOnlyUsersChampionships() {
        List<Team> teams1 = createAndPersistTeams("A", "B", "C", "D");
        Campeonato campeonato1 = Campeonato.createCampeonato("Campeonato 1", teams1);
        campeonato1.setUser(testUser);
        campRepository.save(campeonato1);

        List<Team> teams2 = createAndPersistTeams("E", "F", "G", "H");
        Campeonato campeonato2 = Campeonato.createCampeonato("Campeonato 2", teams2);
        campeonato2.setUser(anotherUser);
        campRepository.save(campeonato2);
        List<Campeonato> encontrados = campRepository.findAllByUserId(testUser.getId());

        assertThat(encontrados).isNotNull().hasSize(1).extracting(Campeonato::getName).containsExactly("Campeonato 1");
    }
}
