package br.ifsp.demo.model;

import jakarta.persistence.*;
import jakarta.servlet.http.Part;
import lombok.Getter;
import org.springframework.security.core.parameters.P;

import java.util.ResourceBundle;
import java.util.UUID;

@Entity
public class Partida {
    @Id
    private UUID uuid;
    @ManyToOne
    @JoinColumn(name = "team_a_id")
    private Team teamA;
    @ManyToOne
    @JoinColumn(name = "team_b_id")
    private Team teamB;
    @ManyToOne
    @JoinColumn(name = "team_vencedor_id")
    private Team winner;
    private boolean isFinished;
    @ManyToOne
    @JoinColumn(name = "fase_id")
    private Fase fase;
    private int chave;




    public Partida(Team teamA, Team teamB){
        this.teamA = teamA;
        this.teamB = teamB;
        isFinished = false;
        uuid = UUID.randomUUID();
    }

    protected Partida() {

    }


    public Team getWinner() {
        return this.winner;
    }

    public void setWinner(Team winner){
        if(winner.equals(teamA) || winner.equals(teamB)){
            this.winner = winner;
            this.isFinished = true;
        }
        else{
            throw new IllegalArgumentException("Vencedor deve ser um dos times da partida!");
        }
    }

    public boolean isFinished() {

        return this.isFinished;
    }

    public UUID getId() {
        return uuid;
    }

    public Team getTeamA(){
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }
    public int getChave() {return chave;}
    public void setChave(int chave) {
        this.chave = chave;
    }
}
