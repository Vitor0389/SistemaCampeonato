package br.ifsp.demo;

import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.services.CampeonatoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


public class CampeonatoServiceTest {

    @Test
    @DisplayName("Testando se há sucesso na criação do campeonato com 32 times.")
    void testingSuccessWith32Teams(){
        Team teams = List.of(new Team(1, "Corinthians"),
                            new Team(2, "São Paulo"),
                            new Team(3, "Santos"),
                            new Team(4, "Flamengo"));

        CampeonatoService service = new CampeonatoService();
        Campeonato campeonato = service.createCampeonato(teams);

        assertThat(campeonato.getId()).isNotNull();
        assertThat(campeonato.getTeams).hasSize(4);
        assertThat(campeonato.getFases().get(0).getPartidas()).hasSize(2);

    }
}
