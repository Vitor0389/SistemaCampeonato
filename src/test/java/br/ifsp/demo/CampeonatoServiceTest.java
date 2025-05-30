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

    private static Stream<Arguments> provide2Teams() {
        List<Team> teams = IntStream.range(1, 3)
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
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando se há sucesso na criação do campeonato com 32 times.")
    @MethodSource("provide32Teams")
    void testingSuccessWith32Teams(List<Team> teams) {

        when(userRepository.getReferenceById(any())).thenReturn(userTest);
        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());

        assertThat(campeonato.getId()).isNotNull();
        assertThat(campeonato.getTimes()).hasSize(32);
        assertThat(campeonato.getFasesList().getFirst()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas()).hasSize(16);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Tag("Functional")
    @Test
    @DisplayName("Testando se há sucesso na criação de campeonato com 2 times.")
    void testingSucessWith2Teams() {

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
    @Tag("Functional")
    @Test
    @DisplayName("Testando se o campeonato exibe erro em time lançado com erro")
    void testingInvalidTeams() {

        UUID uuid = UUID.randomUUID();
        List<Team> teams = List.of(
                new Team(uuid, "São Paulo"),
                new Team(uuid, "Corinthians")
        );

        assertThatThrownBy(() -> {
                    service.createCampeonato("Teste", teams, userTest.getId());
                }
        ).isInstanceOf(IllegalStateException.class);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Tag("Functional")
    @Test
    @DisplayName("Testando se o campeonato exibe erro ao tentar criar times em potência diferente de 2")
    void testingInvalidNumberOfTeams() {

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
    @Tag("Functional")
    @Test
    @DisplayName("Testando se o campeonato exibe erro ao tentar passar uma lista vazia de times")
    void testingInvalidList() {

        List<Team> teams = List.of();

        assertThatThrownBy(() -> {
                    service.createCampeonato("Teste", teams, userTest.getId());
                }
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando se um campeonato com os mesmos times de outro é criado com ID diferente")
    @MethodSource("provide32Teams")
    void testingSimmilarChampionship(List<Team> times) {


        assertThat(service.createCampeonato("Teste 1", times, userTest.getId()).getId()).isNotSameAs(
                service.createCampeonato("Teste 2", times, userTest.getId()).getId());
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando se o sistema registra vitória simples")
    @MethodSource("provide32Teams")
    void testingRegisteringWinner(List<Team> times) {

        Campeonato campeonato = service.createCampeonato("Teste", times, userTest.getId());
        Team winner = campeonato.getTimes().getFirst();
        campeonato.getFasesList().getFirst().getPartidas().getFirst().setWinner(winner);

        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst().getWinner()).isNotNull();
        assertThat(campeonato.getFasesList().getFirst().getPartidas().getFirst().isFinished()).isEqualTo(true);
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando se o sistema cria nova fase a partir dos resultados")
    @MethodSource("provide4Teams")
    public void testingCreateOfNewPhase(List<Team> teams) {

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
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando se com apenas 1 partida vencida a próxima fase ja aparece com o vencedor cadastrado.")
    @MethodSource("provide4Teams")
    public void testingNextPhase(List<Team> times) {

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
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando se o sistema lança erro ao tentar registrar resultado em partida já finalizada")
    @MethodSource("provide4Teams")
    public void testingIfThrowsErrorOnFinishedGame(List<Team> times) {

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
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando se uma partida aceita vencedor nulo (empate)")
    @MethodSource("provide4Teams")
    public void testingIfAcceptsDraw(List<Team> teams) {
        Campeonato campeonato = service.createCampeonato("Teste", teams, userTest.getId());
        Partida partida = campeonato.getFasesList().getFirst().getPartidas().getFirst();

        assertThatThrownBy(() -> {
                    campeonato.registerResult(partida.getId(), null);
                }
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("Partidas não podem terminar empatadas, deve haver um vencedor!");
    }

    @Tag("TDD")
    @Tag("Unit Test")
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando registro de resultado em partida de id inexistente")
    @MethodSource("provide4Teams")
    public void testingRegisterResultInInvalidMatch(List<Team> teams) {
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
    @Tag("Functional")
    @ParameterizedTest
    @DisplayName("Testando visualização do campeonato em fase inicial!")
    @MethodSource("provide16Teams")
    public void testingCampeonatoInitialView(List<Team> teams) {
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
    @Tag("Functional")
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
    @Tag("Functional")
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
    @Tag("Functional")
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
    @Tag("Functional")
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
    @Tag("Functional")
    @Test
    @DisplayName("Testando visualização de campeonatos vazia para novo usuário")
    public void testingEmptyChampionshipList() {

        when(campeonatoRepository.findAllByUserId(userTest.getId())).thenReturn(Collections.emptyList());

        List<CampeonatoDTO> result = service.findAllCampeonatos(userTest.getId());

        assertThat(result).isEmpty();
    }

    @Tag("Unit Test")
    @Tag("Functional")
    @DisplayName("Testando usuário visualizar detalhes apenas do seu próprio campeonato")
    @ParameterizedTest
    @MethodSource("provide4Teams")
    public void testUserCanViewOwnChampionshipDetails(List<Team> times) {

        Campeonato campeonato = service.createCampeonato("Teste", times, userTest.getId());

        when(campeonatoRepository.findByIdAndUserId(campeonato.getId(), userTest.getId())).thenReturn(Optional.of(campeonato));

        List<FaseDTO> fases = service.viewDetails(campeonato.getId(), userTest.getId());

        assertThat(fases).isNotNull();
        assertThat(fases).hasSizeGreaterThan(0);
        assertThat(fases.getFirst().partidas()).hasSizeGreaterThan(0);


    }

    @Tag("Unit Test")
    @Tag("Functional")
    @Test
    @DisplayName("Testando o retorno de campeonato inexistente")
    void testingViewofNotCreatedChampionship() {
        assertThatThrownBy(() -> service.viewDetails(UUID.randomUUID(), userTest.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    public void testingCampeonatoDelete(List<Team> teams) {
        Campeonato campeonato = service.createCampeonato("Paulistão", teams, userTest.getId());
        UUID campeonatoId = campeonato.getId();

        when(campeonatoRepository.findById(campeonatoId))
                .thenReturn(Optional.of(campeonato));

        service.deleteCampeonato(campeonatoId, userTest.getId());

        when(campeonatoRepository.findById(campeonatoId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.viewDetails(campeonatoId, userTest.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Campeonato deve existir!");
    }

    @Tag("Unit Test")
    @Tag("Functional")
    @Test
    @DisplayName("Dado um ID inválido, quando tento excluir, então o sistema lança exceção de 'campeonato não encontrado'")
    public void testDeleteCampeonatoWithInvalidId() {
        UUID invalidId = UUID.randomUUID();


        assertThatThrownBy(() -> service.deleteCampeonato(invalidId, invalidId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Tag("Unit Test")
    @Tag("Structural")
    @ParameterizedTest
    @DisplayName("Testando criação com mais de 32 times")
    @MethodSource("provide32Teams")
    public void testingCreateCamepeonatoWithMoreThan32Times(List<Team> teams) {
        Team team33 = new Team(UUID.randomUUID(), "Time 33");
        teams.add(team33);

        assertThatThrownBy(() -> service.createCampeonato("Time", teams, userTest.getId())).isInstanceOf(IllegalArgumentException.class);

    }


    @Tag("Unit Test")
    @Tag("Structural")
    @Test
    @DisplayName("Testando criação com lista de times vazia")
    public void testingCreateCampeonatoWithEmptyList() {
        List<Team> teams = Collections.emptyList();
        assertThatThrownBy(() -> service.createCampeonato("Time", teams, userTest.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @Tag("Unit Test")
    @Tag("Structural")
    @ParameterizedTest
    @DisplayName("Testando criação de nova fase com apenas um time na lista de vencedores")
    @MethodSource("provide2Teams")
    public void testingCreateNewFaseAfterFinal(List<Team> teams) {

        Campeonato campeonato = service.createCampeonato("Time", teams, userTest.getId());

        Fase fase1 = campeonato.getCurrentFase();

        campeonato.registerResult(fase1.getPartidas().getFirst().getId(), fase1.getPartidas().getFirst().getTeamA());


        assertThat(campeonato.getFasesList()).hasSize(1);
    }

    @Tag("Unit Test")
    @Tag("Structural")
    @ParameterizedTest
    @DisplayName("Testando ver vencedor de campeonato")
    @MethodSource("provide2Teams")
    public void testingGetWinner(List<Team> teams) {
        Team team1 = teams.get(0);
        Campeonato campeonato = service.createCampeonato("Time", teams, userTest.getId());
        assertThatThrownBy(() -> campeonato.getWinner()).isInstanceOf(NoSuchElementException.class);

        Fase fase1 = campeonato.getCurrentFase();

        campeonato.registerResult(fase1.getPartidas().getFirst().getId(), fase1.getPartidas().getFirst().getTeamA());


        assertThat(campeonato.getWinner()).isEqualTo(team1);
    }


    @Tag("Unit Test")
    @Tag("Structural")
    @ParameterizedTest
    @DisplayName("testando get user")
    @MethodSource("provide2Teams")
    public void testingGetUser(List<Team> teams) {

        when(userRepository.getReferenceById(userTest.getId())).thenReturn(userTest);
        Campeonato campeonato = service.createCampeonato("Time", teams, userTest.getId());

        assertThat(campeonato.getUser()).isEqualTo(userTest);
    }

    @Tag("Unit Test")
    @Tag("Structural")
    @Test
    @DisplayName("Testando criar campeonato com lista de times nula")
    public void testingCreateCampeonatoWithNullTeams() {
        assertThatThrownBy(() -> service.createCampeonato("Time", null, userTest.getId())).isInstanceOf(NullPointerException.class);
    }

    @Tag("Unit Test")
    @Tag("Structural")
    @ParameterizedTest
    @DisplayName("testando registrar vencedor de um dos times fora da partida")
    @MethodSource("provide4Teams")
    public void testingDifferentWinnerOnTheMatch(List<Team> teams) {

        Campeonato campeonato = service.createCampeonato("Time", teams, userTest.getId());
        Fase fase1 = campeonato.getCurrentFase();
        Team team = teams.get(2);

        assertThatThrownBy(() -> {
            campeonato.registerResult(fase1.getPartidas().getFirst().getId(), team);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Tag("Unit Test")
    @Tag("Structural")
    @ParameterizedTest
    @DisplayName("Testando findByIdAndUserId")
    @MethodSource("provide2Teams")
    public void testingFindByIdAndUserId(List<Team> teams) {
        Campeonato campeonatoCriado = service.createCampeonato("Champions League", teams, userTest.getId());

        when(campeonatoRepository.findByIdAndUserId(campeonatoCriado.getId(), userTest.getId())).thenReturn(Optional.of(campeonatoCriado));
        CampeonatoDTO campeonatoRecebido = service.findByIdAndUserId(campeonatoCriado.getId(), userTest.getId());
        assertThat(campeonatoRecebido).isNotNull();
    }

    @Tag("Unit Test")
    @Tag("Structural")
    @ParameterizedTest
    @DisplayName("Testando findByIdAndUserId")
    @MethodSource("provide2Teams")
    public void testingNotFindByIdAndUserId(List<Team> teams) {
        Campeonato campeonatoCriado = service.createCampeonato("Champions League", teams, userTest.getId());

        assertThatThrownBy(() -> service.findByIdAndUserId(UUID.randomUUID(), userTest.getId())).isInstanceOf(NoSuchElementException.class);
    }

    @Tag("Unit Test")
    @Tag("Structural")
    @ParameterizedTest
    @DisplayName("Testando deletar campeonato do BD")
    @MethodSource("provide2Teams")
    public void testingDeleteCampeonato(List<Team> teams) {

        Campeonato campeonato = service.createCampeonato("Time", teams, userTest.getId());
        Optional<Campeonato> optionalCampeonato = Optional.of(campeonato);

        
        when(campeonatoRepository.findByIdAndUserId(eq(userTest.getId()), eq(campeonato.getId())))
                .thenReturn(optionalCampeonato)
                .thenReturn(Optional.empty());


        service.deleteCampeonato(campeonato.getId(), userTest.getId());


        verify(campeonatoRepository).deleteById(campeonato.getId());

        assertThatThrownBy(() -> {
            service.deleteCampeonato(campeonato.getId(), userTest.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }
}
