package br.ifsp.demo;

import br.ifsp.demo.DTOs.CampeonatoDTO;
import br.ifsp.demo.DTOs.FaseDTO;
import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Fase;
import br.ifsp.demo.model.Partida;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.services.CampeonatoService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;



import java.util.List;
import java.util.NoSuchElementException;
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

    private static Stream<Arguments> provide16Teams() {
        List<Team> teams = IntStream.range(1, 17)
                .mapToObj(i -> new Team(UUID.randomUUID(), "Time " + i))
                .collect(Collectors.toList());

        return Stream.of(Arguments.of(teams));
    }

    private static Stream<Arguments> provide4Teams() {
        List<Team> teams = IntStream.range(1, 5)
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
        assertThat(campeonato.getFasesList().getFirst().getPartidas()).hasSize(16);
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
        Team winner = campeonato.getTimes().getFirst();
        campeonato.getFasesList().getFirst().getPartidas().getFirst().setWinner(winner);

        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst().getWinner()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst().isFinished()).isEqualTo(true);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Test
    @DisplayName("Testando se o sistema cria nova fase a partir dos resultados")
    public void testingCreateOfNewPhase(){
        List<Team> teams = List.of(
                new Team(UUID.randomUUID(), "São Paulo"),
                new Team(UUID.randomUUID(), "Corinthians"),
                new Team(UUID.randomUUID(), "Santos"),
                new Team(UUID.randomUUID(), "Flamengo")
        );

        Campeonato campeonato = service.createCampeonato("Teste", teams);
        Fase fase1 = campeonato.getFasesList().getFirst();
        Partida partida1 = fase1.getPartidas().get(0);
        Partida partida2 = fase1.getPartidas().get(1);
        campeonato.registerResult(partida1.getId(), partida1.getTeamA());
        campeonato.registerResult(partida2.getId(), partida2.getTeamB());

        assertThat(campeonato.getFasesList()).hasSize(2);
        Fase fase2 = campeonato.getFasesList().get(1);
        assertThat(fase2.getPartidas()).hasSize(1);

        Partida novaPartida = fase2.getPartidas().getFirst();
        assertThat(novaPartida.getTeamA()).isEqualTo(partida1.getTeamA());
        assertThat(novaPartida.getTeamB()).isEqualTo(partida2.getTeamB());
        assertThat(novaPartida.isFinished()).isFalse();

    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se com apenas 1 partida vencida a próxima fase ja aparece com o vencedor cadastrado.")
    @MethodSource("provide4Teams")
    public void testingNextPhase(List <Team> times){

        Campeonato campeonato = service.createCampeonato("Teste", times);
        Fase fase1 = campeonato.getFasesList().getFirst();
        Partida partida1 = fase1.getPartidas().getFirst();
        Partida partida2 = fase1.getPartidas().get(1);

        campeonato.registerResult(partida1.getId(), partida1.getTeamA());
        campeonato.registerResult(partida2.getId(), partida2.getTeamB());


        assertThat(campeonato.getFasesList().get(1)).isNotNull();
        assertThat(campeonato.getFasesList().get(1).getPartidas().getFirst().getTeamA()).isNotNull();
        assertThat(campeonato.getFasesList().get(1).getPartidas().getFirst().getTeamB()).isNotNull();
        assertThat(campeonato.getFasesList().get(1).getPartidas()).hasSize(1);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se o sistema lança erro ao tentar registrar resultado em partida já finalizada")
    @MethodSource("provide4Teams")
    public void testingIfThrowsErrorOnFinishedGame(List<Team> times){

        Campeonato campeonato = service.createCampeonato("Teste", times);
        Fase fase1 = campeonato.getFasesList().getFirst();
        Partida partida1 = fase1.getPartidas().getFirst();

        campeonato.registerResult(partida1.getId(), partida1.getTeamA());


        assertThatThrownBy(() -> {
            campeonato.registerResult(partida1.getId(), partida1.getTeamA());
                }
        ).isInstanceOf(IllegalStateException.class);

    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se uma partida aceita vencedor nulo (empate)")
    @MethodSource("provide4Teams")
    public void testingIfAcceptsDraw(List<Team> teams){
        Campeonato campeonato = service.createCampeonato("Teste", teams);
        Partida partida = campeonato.getFasesList().getFirst().getPartidas().getFirst();

        assertThatThrownBy(() -> {
            campeonato.registerResult(partida.getId(), null);
        }
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("Partidas não podem terminar empatadas, deve haver um vencedor!");
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando registro de resultado em partida de id inexistente")
    @MethodSource("provide4Teams")
    public void testingRegisterResultInInvalidMatch(List<Team> teams){
        Campeonato campeonato = service.createCampeonato("Teste", teams);
        Team teamA = teams.get(0);
        Team teamB = teams.get(1);
        Partida partida = new Partida(teamA, teamB);

        assertThatThrownBy(() -> {
            campeonato.registerResult(partida.getId(), teamA);
                }
        ).isInstanceOf(NoSuchElementException.class).hasMessage("Partida não encontrada, por favor informe um ID de partida válido!");
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando visualização do campeonato em fase inicial!")
    @MethodSource("provide16Teams")
    public void testingCampeonatoInitialView(List<Team> teams){
        Campeonato campeonato = service.createCampeonato("Teste", teams);

        CampeonatoDTO dto = service.viewDetails(campeonato.getId());

        assertThat(dto).isNotNull();
        assertThat(dto.fases()).hasSize(1);
        assertThat(dto.fases().getFirst().partidas()).hasSize(8);

        assertThat(dto.fases().get(0).partidas())
                .allSatisfy(partida -> assertThat(partida.vencedor()).isNull());
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando visualização do campeonato após uma fase concluida")
    @MethodSource("provide4Teams")
    public void testingNextView(List<Team> times){

        Campeonato campeonato = service.createCampeonato("Teste", times);
        CampeonatoDTO campeonatoDTO = service.viewDetails(campeonato.getId());

        Fase fase1 = campeonato.getFasesList().getFirst();

        Partida partida1 = fase1.getPartidas().getFirst();
        Partida partida2 = fase1.getPartidas().get(1);

        campeonato.registerResult(partida1.getId(), partida1.getTeamA());
        campeonato.registerResult(partida2.getId(), partida2.getTeamA());

        Fase fase2 = campeonato.getFasesList().get(1);

        FaseDTO fase1DTO = campeonatoDTO.fases().getFirst();
        FaseDTO fase2DTO = campeonatoDTO.currentFase();

        TeamDTO team1 = campeonatoDTO.teams().getFirst();
        TeamDTO team2 = campeonatoDTO.teams().get(2);

        assertThat(fase1DTO.vencedores().getFirst()).isEqualto(team1);
        assertThat(fase1DTO.vencedores().get(1)).isEqualTo(team2);

        assertThat(fase2DTO.partidas()).hasSize(1);

    }
}
