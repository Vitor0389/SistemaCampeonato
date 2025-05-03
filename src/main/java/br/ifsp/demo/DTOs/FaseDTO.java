package br.ifsp.demo.DTOs;

import br.ifsp.demo.model.Team;

import java.util.List;

public record FaseDTO(
        String name,
        List<PartidaDTO> partidas,
        List<TeamDTO> vencedores
) {
}
