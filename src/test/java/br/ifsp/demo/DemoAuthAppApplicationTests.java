package br.ifsp.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest
class DemoAuthAppApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Testando se há sucesso na criação do campeonato com 32 times.")
    void testingSuccessWith32Teams(){

        assertThat(CampeonatoService.createCampeonato()).isEqualto(true);

    }

}
