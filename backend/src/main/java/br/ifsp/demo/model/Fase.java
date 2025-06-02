package br.ifsp.demo.model;

import jakarta.persistence.*;
import jakarta.servlet.http.Part;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Fase {
    @Id
    private UUID id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fase_id")
    private List<Partida> partidas;
    @ManyToOne
    @JoinColumn(name = "campeonato_id")  // Chave estrangeira para Campeonato
    private Campeonato campeonato;

    @ManyToMany
    @JoinTable(name = "fase_vencedores",
            joinColumns = @JoinColumn(name = "fase_id"),
            inverseJoinColumns = @JoinColumn(name = "time_id"))
    private List<Team> vencedores = new ArrayList<>();


    public Fase(String name, List<Partida> partidas) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.partidas = partidas;

    }

    protected Fase() {

    }

    public void addWinner(Team team){
        this.vencedores.add(team);
    }

    public List<Partida> getPartidas(){
        return this.partidas;
    }

    public List<Team> getVencedores() {
        if(vencedores.isEmpty()){
            return List.of();
        }
        return this.vencedores = partidas.stream()
                .map(Partida::getWinner)
                .collect(Collectors.toList());

    }

    public String getName() {
        return name;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }
}
