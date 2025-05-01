package br.ifsp.demo.model;

import jakarta.servlet.http.Part;
import org.springframework.security.core.parameters.P;

import java.util.ResourceBundle;

public class Partida {
    private final Team teamA;
    private final Team teamB;
    private Resultado resultado;

    public Partida(Team teamA, Team teamB){
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public void setResult(Resultado resultado){
        this.resultado = resultado;
    }
}
