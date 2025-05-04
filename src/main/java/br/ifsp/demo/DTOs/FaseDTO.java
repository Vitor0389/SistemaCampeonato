package br.ifsp.demo.DTOs;

import br.ifsp.demo.model.Fase;
import br.ifsp.demo.model.Partida;
import br.ifsp.demo.model.Team;

import java.util.List;

public record FaseDTO(
        String name,
        List<PartidaDTO> partidas,
        List<TeamDTO> vencedores
) {

    public FaseDTO(String name, List<PartidaDTO> partidas, List<TeamDTO> vencedores) {
        this.name = name;
        this.partidas = partidas;
        this.vencedores = vencedores;
    }

    public FaseDTO(Fase fase){
        this(fase.getName(),
                fase.getPartidas().stream().map(PartidaDTO::new).toList(),
                fase.getVencedores().stream().map(TeamDTO::new).toList()
        );
    }
}
