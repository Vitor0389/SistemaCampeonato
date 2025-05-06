package br.ifsp.demo.DTOs;

import br.ifsp.demo.model.Team;

import java.util.List;

public record CampeonatoRequestDTO(
        String name,
        List<TeamDTO> teams
) {
}
