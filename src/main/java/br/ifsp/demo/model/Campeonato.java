package br.ifsp.demo.model;

import jakarta.servlet.http.Part;

import java.util.*;

public class Campeonato {
    private UUID id;
    private String name;
    private List<Team> times;
    private List<Fase> fases;
    private Fase currentFase;

    private Campeonato(String name, List<Team> teams) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.times = teams;
        this.fases = new ArrayList<>();
        crateInitialFase(times);
    }

    private static void validateTeams(List<Team> times) {
        Set<UUID> idsVistos = new HashSet<>();
        boolean possuiDuplicado = false;

        if(times.size() < 2 || times.size() > 32)
            throw new IllegalArgumentException("A quantidade de times inválida!");

        if((times.size() & (times.size() - 1))!= 0)
            throw new IllegalArgumentException("O número de times deve ser potência de 2!");

        for(Team team : times){

            if(!idsVistos.add(team.getId())){
                possuiDuplicado = true;
            }

        }

        if(possuiDuplicado){
            throw new IllegalStateException("Dois times não podem ter o mesmo ID!");
        }

        if(times.isEmpty()){
            throw new IllegalArgumentException("O número de times deve ser no mínimo 2!");
        }

    }

    private void crateInitialFase(List<Team> times) {
        List<Partida> partidas = new ArrayList<>();

        for (int i = 0; i < times.size(); i += 2) {
            Partida partida = new Partida(times.get(i), times.get(i + 1));
            partidas.add(partida);
        }

        Fase faseInicial = new Fase("Fase Inicial", partidas);
        this.fases.add(faseInicial);
        this.currentFase = faseInicial;
    }

    public void registerResult(UUID id, Team team){
        Optional<Partida> partida = findPartidaByid(id);

        if (partida.isEmpty()) throw new NoSuchElementException("Id não corresponde a nenhuma partida.");
        partida.get().setWinner(team);
    }

    private void createNewFase(Partida partida){
        if (isCurrentFaseFinished()) {
            List<Team> vencedores = currentFase.getVencedores();
            if (vencedores.size() > 1) {
                Fase novaFase = Fase.criarComTimes(vencedores);
                novaFase.associarAoCampeonato(this);
                fases.add(novaFase);
            }
        }
    }

    private boolean isCurrentFaseFinished(){
        return currentFase.getPartidas().stream().allMatch(Partida::isFinished);
    };

    private Optional<Partida> findPartidaByid(UUID id){
       return fases.stream()
                .flatMap(fase -> fase.getPartidas().stream())
                .filter(partida -> partida.getId().equals(id))
                .findFirst();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Team> getTimes() {
        return times;
    }

    public List<Fase> getFasesList() {
        return fases;
    }

    public static Campeonato createCampeonato(String name, List<Team> times){

        validateTeams(times);

        return new Campeonato(name, times);
    }
}
