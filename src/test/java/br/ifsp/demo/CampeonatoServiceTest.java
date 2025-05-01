package br.ifsp.demo;

import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.services.CampeonatoService;
import org.junit.jupiter.api.DisplayName;
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

    @ParameterizedTest
    @DisplayName("Testando se há sucesso na criação do campeonato com 32 times.")
    @MethodSource("provide32Teams")
    void testingSuccessWith32Teams(List<Team> teams){


        Campeonato campeonato = service.createCampeonato("Teste", teams);

        assertThat(campeonato.getId()).isNotNull();
        assertThat(campeonato.getTimes()).hasSize(32);
        assertThat(campeonato.getFasesList().get(0)).isNotNull;
    }

    @Test
    @DisplayName("Testando se há sucesso na criação de campeonato com 2 times.")
    void testingSucessWith2Teams(){

        assertThat(campeonatoService.createCampeonato()).isEqualTo(true);

    }
}
