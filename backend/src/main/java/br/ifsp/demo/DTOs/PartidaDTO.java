package br.ifsp.demo.DTOs;

import br.ifsp.demo.model.Partida;
import br.ifsp.demo.model.Team;

import java.util.UUID;

public record PartidaDTO(
        Team teamA,
        Team teamB,
        Team winner,
        UUID uuid
) {
    public PartidaDTO(Team teamA, Team teamB, Team winner, UUID uuid) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.winner = winner;
        this.uuid = uuid;
    }

    public PartidaDTO(Partida partida){
        this(partida.getTeamA(), partida.getTeamB(), partida.getWinner(), partida.getId());
    }
}
