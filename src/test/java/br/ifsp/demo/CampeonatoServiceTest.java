package br.ifsp.demo;

import br.ifsp.demo.services.CampeonatoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


public class CampeonatoServiceTest {

    private CampeonatoService campeonatoService;


    @Test
    @DisplayName("Testando se há sucesso na criação do campeonato com 32 times.")
    void testingSuccessWith32Teams(){

        assertThat(campeonatoService.createCampeonato()).isEqualTo(true);
    }
}
