package br.ifsp.demo.DTOs;

import java.util.List;
import java.util.UUID;

public record CampeonatoDTO(
        UUID id,
        String name,
        List<FaseDTO> fases,
        List<TeamDTO> teams,
        FaseDTO currentFase
) {
}
