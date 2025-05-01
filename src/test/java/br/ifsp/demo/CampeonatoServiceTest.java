package br.ifsp.demo;

import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.services.CampeonatoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


public class CampeonatoServiceTest {

    private CampeonatoService service = new CampeonatoService();

    @Test
    @DisplayName("Testando se há sucesso na criação do campeonato com 32 times.")
    void testingSuccessWith32Teams(){
        List<Team> teams = List.of(new Team(UUID.randomUUID(), "Corinthians"),
                                    new Team(UUID.randomUUID(), "São Paulo"),
                                    new Team(UUID.randomUUID(), "Santos"),
                                    new Team(UUID.randomUUID(), "Flamengo"));

        Campeonato campeonato = service.createCampeonato(teams);

        assertThat(campeonato.getId()).isNotNull();
        assertThat(campeonato.getTeams).hasSize(4);
        assertThat(campeonato.getFases().get(0).getPartidas()).hasSize(2);

    }
}
