package br.ifsp.demo.model;

import jakarta.servlet.http.Part;
import org.springframework.security.core.parameters.P;

import java.util.ResourceBundle;

public class Partida {
    private final Team teamA;
    private final Team teamB;
    private Team winner;

    private boolean isFinished;



    public Partida(Team teamA, Team teamB){
        this.teamA = teamA;
        this.teamB = teamB;
        isFinished = false;
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

    public boolean IsFinished() {

        return this.isFinished;
    }
}
