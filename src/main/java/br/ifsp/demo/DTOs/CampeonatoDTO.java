package br.ifsp.demo.DTOs;

import br.ifsp.demo.model.Campeonato;

import java.util.List;
import java.util.UUID;

public record CampeonatoDTO(
        UUID id,
        String name,
        List<FaseDTO> fases,
        List<TeamDTO> teams,
        FaseDTO currentFase


) {
    public CampeonatoDTO(UUID id, String name, List<FaseDTO> fases, List<TeamDTO> teams, FaseDTO currentFase) {
        this.id = id;
        this.name = name;
        this.fases = fases;
        this.teams = teams;
        this.currentFase = currentFase;
    }

    public CampeonatoDTO(Campeonato campeonato){
        this(
                campeonato.getId(),
                campeonato.getName(),
                campeonato.getFasesList(),
                campeonato.getTimes(),
                campeonato.getCurrentFase()
        );
    }
}
