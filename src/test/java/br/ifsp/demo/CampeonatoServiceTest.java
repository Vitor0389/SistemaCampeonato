package br.ifsp.demo;

import br.ifsp.demo.DTOs.CampeonatoDTO;
import br.ifsp.demo.DTOs.FaseDTO;
import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Fase;
import br.ifsp.demo.model.Partida;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.CampeonatoRepository;
import br.ifsp.demo.repository.FakeCampeonatoRepository;
import br.ifsp.demo.security.user.JpaUserRepository;
import br.ifsp.demo.security.user.Role;
import br.ifsp.demo.security.user.User;
import br.ifsp.demo.services.CampeonatoService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
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

    @Mock
    private CampeonatoRepository campeonatoRepository;
    @Mock
    private JpaUserRepository userRepository;
    @InjectMocks
    private CampeonatoService service;
    private User userTest = new User(UUID.randomUUID(), "Teste", "da Silva", "teste@email.com", "teste123", Role.ADMIN);



    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se há sucesso na criação do campeonato com 32 times.")
    @MethodSource("provide32Teams")
    void testingSuccessWith32Teams(List<Team> teams){

        when(userRepository.getReferenceById(any())).thenReturn(userTest);
        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());

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

        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());
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
            service.createCampeonato("Teste", teams,userTest.getId());
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
                    service.createCampeonato("Teste", teams, userTest.getId());
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
                    service.createCampeonato("Teste", teams, userTest.getId());
                }
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se um campeonato com os mesmos times de outro é criado com ID diferente")
    @MethodSource("provide32Teams")
    void testingSimmilarChampionship(List<Team> times){


        assertThat(service.createCampeonato("Teste 1", times, userTest.getId()).getId()).isNotSameAs(
                service.createCampeonato("Teste 2", times, userTest.getId()).getId());
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se o sistema registra vitória simples")
    @MethodSource("provide32Teams")
    void testingRegisteringWinner(List<Team> times){

        Campeonato campeonato = service.createCampeonato("Teste", times, userTest.getId());
        Team winner = campeonato.getTimes().getFirst();
        campeonato.getFasesList().getFirst().getPartidas().getFirst().setWinner(winner);

        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst().getWinner()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst().isFinished()).isEqualTo(true);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando se o sistema cria nova fase a partir dos resultados")
    @MethodSource("provide4Teams")
    public void testingCreateOfNewPhase(List<Team> teams){

        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());
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

        Campeonato campeonato = service.createCampeonato("Teste", times, userTest.getId());
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

        Campeonato campeonato = service.createCampeonato("Teste", times, userTest.getId());
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
        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());
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
        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());
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
        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());
        when(campeonatoRepository.findByIdAndUserId(campeonato.getId(), userTest.getId())).thenReturn(Optional.of(campeonato));
        List<FaseDTO> dto = service.viewDetails(campeonato.getId(), userTest.getId());


        assertThat(dto).isNotNull();
        assertThat(dto).hasSize(1);
        assertThat(dto.getFirst().partidas()).hasSize(8);

        assertThat(dto.getFirst().partidas())
                .allSatisfy(partida -> assertThat(partida.winner()).isNull());
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando visualização do campeonato após uma fase concluída")
    @MethodSource("provide4Teams")
    void testingNextView(List<Team> times) {
        Campeonato campeonato = service.createCampeonato("Teste", times, userTest.getId());
        when(campeonatoRepository.findByIdAndUserId(campeonato.getId(), userTest.getId())).thenReturn(Optional.of(campeonato));


        Fase fase1 = campeonato.getCurrentFase();
        campeonato.registerResult(fase1.getPartidas().get(0).getId(), fase1.getPartidas().get(0).getTeamA());
        campeonato.registerResult(fase1.getPartidas().get(1).getId(), fase1.getPartidas().get(1).getTeamA());

        List<FaseDTO> fases = service.viewDetails(campeonato.getId(), userTest.getId());

        assertThat(fases).hasSize(2);

        FaseDTO fase1DTO = fases.get(0);
        FaseDTO fase2DTO = fases.get(1);

        assertThat(fase1DTO.vencedores()).hasSize(2);
        assertThat(fase2DTO.partidas()).hasSize(1);
    }


    @Tag("TDD")
    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando campeonato completo")
    @MethodSource("provide4Teams")
    void testingFinishedChampionship(List<Team> times) {
        Campeonato campeonato = service.createCampeonato("Teste", times, userTest.getId());
        when(campeonatoRepository.findByIdAndUserId(campeonato.getId(), userTest.getId())).thenReturn(Optional.of(campeonato));

        List<FaseDTO> fases = service.viewDetails(campeonato.getId(), userTest.getId());

        assertThat(campeonato).isNotNull();
        assertThat(fases).isNotNull();
    }


    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando lista de vencedores")
    @MethodSource("provide4Teams")
    void testingWinnersList(List<Team> teams) {
        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());

        Fase fase1 = campeonato.getCurrentFase();
        campeonato.registerResult(fase1.getPartidas().get(0).getId(), fase1.getPartidas().get(0).getTeamA());
        campeonato.registerResult(fase1.getPartidas().get(1).getId(), fase1.getPartidas().get(1).getTeamA());

        assertThat(campeonato.getCurrentFase().getVencedores()).isEmpty();
    }

    @Tag("Unit Test")
    @ParameterizedTest
    @DisplayName("Testando a visualização de campeonatos de um usuário")
    @MethodSource("provide4Teams")
    public void testingUserChampionshipView(List<Team> teams) {

        Campeonato campeonato1 = Campeonato.createCampeonato("Teste1", teams);
        Campeonato campeonato2 = Campeonato.createCampeonato("Teste2", teams);
        Campeonato campeonato3 = Campeonato.createCampeonato("Teste3", teams);

        campeonato1.setUser(userTest);
        campeonato2.setUser(userTest);
        campeonato3.setUser(userTest);

        List<Campeonato> campeonatos = List.of(campeonato1, campeonato2, campeonato3);
        when(campeonatoRepository.findAllByUserId(userTest.getId())).thenReturn(campeonatos);


        List<CampeonatoDTO> result = service.findAllCampeonatos(userTest.getId());




        assertThat(result).hasSize(3);

    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Test
    @DisplayName("Testando visualização de campeonatos vazia para novo usuário")
    public void testingEmptyChampionshipList() {

        when(campeonatoRepository.findAllByUserId(userTest.getId())).thenReturn(Collections.emptyList());

        List<CampeonatoDTO> result = service.findAllCampeonatos(userTest.getId());

        assertThat(result).isEmpty();
    }

    @Tag("Unit Test")
    @DisplayName("Testando usuário visualizar detalhes apenas do seu próprio campeonato")
    @ParameterizedTest
    @MethodSource("provide4Teams")
    public void testUserCanViewOwnChampionshipDetails(List<Team> times){

        Campeonato campeonato = service.createCampeonato("Teste", times, userTest.getId());

        when(campeonatoRepository.findByIdAndUserId(campeonato.getId(), userTest.getId())).thenReturn(Optional.of(campeonato));

        List<FaseDTO> fases = service.viewDetails(campeonato.getId(), userTest.getId());

        assertThat(fases).isNotNull();
        assertThat(fases).hasSizeGreaterThan(0);
        assertThat(fases.getFirst().partidas()).hasSizeGreaterThan(0);


    }

    @Tag("Unit Test")
    @Test
    @DisplayName("Testando o retorno de campeonato inexistente")
    void testingViewofNotCreatedChampionship()
    {
        assertThatThrownBy(() -> service.viewDetails(UUID.randomUUID(), userTest.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    public void testingCampeonatoDelete(List<Team> teams) {
        Campeonato campeonato = service.createCampeonato("Paulistão", teams, userTest.getId());
        UUID campeonatoId = campeonato.getId();

        when(campeonatoRepository.findById(campeonatoId))
                .thenReturn(Optional.of(campeonato));

        service.deleteCampeonato(campeonatoId , userTest.getId());

        when(campeonatoRepository.findById(campeonatoId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.viewDetails(campeonatoId, userTest.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Campeonato deve existir!");
    }

    @Test
    @DisplayName("Dado um ID inválido, quando tento excluir, então o sistema lança exceção de 'campeonato não encontrado'")
    public void testDeleteCampeonatoWithInvalidId() {
        UUID invalidId = UUID.randomUUID();


        assertThatThrownBy(() -> service.deleteCampeonato(invalidId, invalidId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @ParameterizedTest
    @DisplayName("Testando se o campeonato funciona")
    @MethodSource("provide16Teams")
    public void testingIfCampeonatoWorks(List<Team> teams) {
        Campeonato campeonato = service.createCampeonato("Champions League", teams, userTest.getId());

        Team team1 = teams.get(0);
        Team team2 = teams.get(1);
        Team team3 = teams.get(2);
        Team team4 = teams.get(3);
        Team team5 = teams.get(4);
        Team team6 = teams.get(5);
        Team team7 = teams.get(6);
        Team team8 = teams.get(7);
        Team team9 = teams.get(8);
        Team team10 = teams.get(9);
        Team team11 = teams.get(10);
        Team team12 = teams.get(11);
        Team team13 = teams.get(12);
        Team team14 = teams.get(13);
        Team team15 = teams.get(14);
        Team team16 = teams.get(15);

        Fase fase1 = campeonato.getCurrentFase();
        assertThat(fase1.getPartidas()).hasSize(8);

        campeonato.registerResult(fase1.getPartidas().get(0).getId(), fase1.getPartidas().get(0).getTeamA());
        campeonato.registerResult(fase1.getPartidas().get(1).getId(), fase1.getPartidas().get(1).getTeamA());

        assertThat(campeonato.getFasesList()).hasSize(1);

        campeonato.registerResult(fase1.getPartidas().get(2).getId(), fase1.getPartidas().get(2).getTeamA());
        campeonato.registerResult(fase1.getPartidas().get(3).getId(), fase1.getPartidas().get(3).getTeamA());
        campeonato.registerResult(fase1.getPartidas().get(4).getId(), fase1.getPartidas().get(4).getTeamA());
        campeonato.registerResult(fase1.getPartidas().get(5).getId(), fase1.getPartidas().get(5).getTeamA());
        campeonato.registerResult(fase1.getPartidas().get(6).getId(), fase1.getPartidas().get(6).getTeamA());
        campeonato.registerResult(fase1.getPartidas().get(7).getId(), fase1.getPartidas().get(7).getTeamA());

        assertThat(campeonato.getFasesList()).hasSize(2);

        Fase fase2 = campeonato.getFasesList().get(1);
        assertThat(fase2).isEqualTo(campeonato.getCurrentFase());
        assertThat(fase2.getPartidas()).hasSize(4);

        assertThat(fase2.getPartidas().get(0).getTeamA()).isEqualTo(team1);
        assertThat(fase2.getPartidas().get(0).getTeamB()).isEqualTo(team3);
        assertThat(fase2.getPartidas().get(1).getTeamA()).isEqualTo(team5);
        assertThat(fase2.getPartidas().get(1).getTeamB()).isEqualTo(team7);
        assertThat(fase2.getPartidas().get(2).getTeamA()).isEqualTo(team9);
        assertThat(fase2.getPartidas().get(2).getTeamB()).isEqualTo(team11);
        assertThat(fase2.getPartidas().get(3).getTeamA()).isEqualTo(team13);
        assertThat(fase2.getPartidas().get(3).getTeamB()).isEqualTo(team15);

        campeonato.registerResult(fase2.getPartidas().get(0).getId(), fase2.getPartidas().get(0).getTeamA());
        campeonato.registerResult(fase2.getPartidas().get(1).getId(), fase2.getPartidas().get(1).getTeamA());
        campeonato.registerResult(fase2.getPartidas().get(2).getId(), fase2.getPartidas().get(2).getTeamA());
        campeonato.registerResult(fase2.getPartidas().get(3).getId(), fase2.getPartidas().get(3).getTeamA());
        assertThat(campeonato.getFasesList()).hasSize(3);

        Fase fase3 = campeonato.getFasesList().get(2);
        assertThat(fase3).isEqualTo(campeonato.getCurrentFase());

        assertThat(fase3.getPartidas()).hasSize(2);

        assertThat(fase3.getPartidas().get(0).getTeamA()).isEqualTo(team1);
        assertThat(fase3.getPartidas().get(0).getTeamB()).isEqualTo(team5);
        assertThat(fase3.getPartidas().get(1).getTeamA()).isEqualTo(team9);
        assertThat(fase3.getPartidas().get(1).getTeamB()).isEqualTo(team13);

        campeonato.registerResult(fase3.getPartidas().get(0).getId(), fase3.getPartidas().get(0).getTeamA());
        campeonato.registerResult(fase3.getPartidas().get(1).getId(), fase3.getPartidas().get(1).getTeamA());
        assertThat(campeonato.getFasesList()).hasSize(4);

        Fase fase4 = campeonato.getFasesList().get(3);
        assertThat(fase4).isEqualTo(campeonato.getCurrentFase());

        assertThat(fase4.getPartidas()).hasSize(1);
        assertThat(fase4.getPartidas().getFirst().getTeamA()).isEqualTo(team1);
        assertThat(fase4.getPartidas().getFirst().getTeamB()).isEqualTo(team9);

        campeonato.registerResult(fase4.getPartidas().getFirst().getId(), fase4.getPartidas().get(0).getTeamA());
        assertThat(campeonato.getWinner()).isEqualTo(team1);







    }

}
