package br.ifsp.demo.DTOs;

import br.ifsp.demo.model.Partida;
import br.ifsp.demo.model.Team;

import java.util.UUID;

public record PartidaDTO(
        TeamDTO teamA,
        TeamDTO teamB,
        TeamDTO winner,
        UUID uuid
) {
    public PartidaDTO(TeamDTO teamA, TeamDTO teamB, TeamDTO winner, UUID uuid) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.winner = winner;
        this.uuid = uuid;
    }

    public PartidaDTO(Partida partida){
        this(
                new TeamDTO(partida.getTeamA()),
                new TeamDTO(partida.getTeamB()),
                partida.getWinner() != null ? new TeamDTO(partida.getWinner()) : null,
                partida.getId()
        );
    }
}

