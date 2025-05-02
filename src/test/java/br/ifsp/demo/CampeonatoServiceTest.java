package br.ifsp.demo;

import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.services.CampeonatoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;



import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;


public class CampeonatoServiceTest {


    private static Stream<Arguments> provide32Teams() {
        List<Team> teams = IntStream.range(1, 33)
                .mapToObj(i -> new Team(UUID.randomUUID(), "Time " + i))
                .collect(Collectors.toList());

        return Stream.of(Arguments.of(teams));
    }

    private final CampeonatoService service = new CampeonatoService();

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se há sucesso na criação do campeonato com 32 times.")
    @MethodSource("provide32Teams")
    void testingSuccessWith32Teams(List<Team> teams){


        Campeonato campeonato = service.createCampeonato("Teste", teams);

        assertThat(campeonato.getId()).isNotNull();
        assertThat(campeonato.getTimes()).hasSize(32);
        assertThat(campeonato.getFasesList().getFirst()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas()).hasSize(8);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Test
    @DisplayName("Testando se há sucesso na criação de campeonato com 2 times.")
    void testingSucessWith2Teams(){

        List<Team> teams = List.of(
                new Team(UUID.randomUUID(), "São Paulo"),
                new Team(UUID.randomUUID(), "Corinthians")
        );

        Campeonato campeonato = service.createCampeonato("Teste", teams);
        assertThat(campeonato.getId()).isNotNull();
        assertThat(campeonato.getTimes()).hasSize(2);
        assertThat(campeonato.getFasesList().getFirst()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas()).hasSize(1);

    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Test
    @DisplayName("Testando se o campeonato exibe erro em time lançado com erro")
    void testingInvalidTeams(){

        UUID uuid = UUID.randomUUID();
        List<Team> teams = List.of(
                new Team(uuid , "São Paulo"),
                new Team(uuid, "Corinthians")
        );

        assertThatThrownBy(() -> {
            service.createCampeonato("Teste", teams);
        }
        ).isInstanceOf(IllegalStateException.class);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Test
    @DisplayName("Testando se o campeonato exibe erro ao tentar criar times em potência diferente de 2")
    void testingInvalidNumberOfTeams(){

        List<Team> teams = List.of(
                new Team(UUID.randomUUID(), "São Paulo"),
                new Team(UUID.randomUUID(), "Corinthians"),
                new Team(UUID.randomUUID(), "Santos"),
                new Team(UUID.randomUUID(), "Palmeiras"),
                new Team(UUID.randomUUID(), "Cruzeiro"),
                new Team(UUID.randomUUID(), "Atletico Mineiro")
        );

        assertThatThrownBy(() -> {
                    service.createCampeonato("Teste", teams);
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Test
    @DisplayName("Testando se o campeonato exibe erro ao tentar passar uma lista vazia de times")
    void testingInvalidList(){

        List<Team> teams = List.of();

        assertThatThrownBy(() -> {
                    service.createCampeonato("Teste", teams);
                }
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se um campeonato com os mesmos times de outro é criado com ID diferente")
    @MethodSource("provide32Teams")
    void testingSimmilarChampionship(List<Team> times){


        assertThat(service.createCampeonato("Teste 1", times).getId()).isNotSameAs(
                service.createCampeonato("Teste 2", times).getId());
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se o sistema registra vitória simples")
    @MethodSource("provide32Teams")
    void testingRegisteringWinner(List<Team> times){

        Campeonato campeonato = service.createCampeonato("Teste", times);

        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst().getWinner()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst().IsFinished()).isEqualTo(true);
    }
}
