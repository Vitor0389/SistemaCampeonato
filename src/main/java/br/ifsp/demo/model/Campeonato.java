package br.ifsp.demo.model;

import org.apache.tomcat.util.http.fileupload.util.LimitedInputStream;

import java.util.*;

public class Campeonato {
    private UUID id;
    private String name;
    private List<Team> times;
    private List<Fase> fases;

    private Campeonato(String name, List<Team> teams) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.times = teams;
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
