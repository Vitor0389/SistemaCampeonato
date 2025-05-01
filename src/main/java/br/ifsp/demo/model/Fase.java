package br.ifsp.demo.model;

import jakarta.servlet.http.Part;

import java.util.Iterator;
import java.util.List;

public class Fase {
    private final String name;
    private final List<Partida> partidas;

    public Fase(String name, List<Partida> partidas) {
        this.name = name;
        this.partidas = partidas;
    }

    public List<Partida> getPartidas(){
        return this.partidas;
    }
}
