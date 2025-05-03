package br.ifsp.demo.model;

import jakarta.servlet.http.Part;
import lombok.Getter;
import org.springframework.security.core.parameters.P;

import java.util.ResourceBundle;
import java.util.UUID;

public class Partida {
    private final Team teamA;
    private final Team teamB;
    private Team winner;
    private final UUID uuid;

    private boolean isFinished;

    public Partida(Team teamA, Team teamB){
        this.teamA = teamA;
        this.teamB = teamB;
        isFinished = false;
        uuid = UUID.randomUUID();
    }


    public Team getWinner() {
        return this.winner;
    }

    public void setWinner(Team winner){
        if(winner.equals(teamA) || winner.equals(teamB)){
            this.winner = winner;
            isFinished = true;
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
}
