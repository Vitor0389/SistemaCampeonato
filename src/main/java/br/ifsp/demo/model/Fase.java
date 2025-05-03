package br.ifsp.demo.model;

import jakarta.servlet.http.Part;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Fase {
    private final String name;
    private final List<Partida> partidas;

    private  List<Team> vencedores = new ArrayList<>();

    private List<Team> times = new ArrayList<>();

    public Fase(String name, List<Partida> partidas) {
        this.name = name;
        this.partidas = partidas;
        for (Partida partida : partidas) {
            this.times.add(partida.getTeamA());
            this.times.add(partida.getTeamB());
        }
    }

    public List<Partida> getPartidas(){
        return this.partidas;
    }

    public List<Team> getVencedores() {

        return this.vencedores = partidas.stream()
                .map(Partida::getWinner)
                .collect(Collectors.toList());

    }

    public List<Team> getTimes() {return this.times;}



}
