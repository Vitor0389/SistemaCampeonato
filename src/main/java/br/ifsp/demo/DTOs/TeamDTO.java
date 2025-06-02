package br.ifsp.demo.DTOs;

import br.ifsp.demo.model.Team;

import java.util.UUID;

public record TeamDTO(
        UUID id,
        String name
) {
    public TeamDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public TeamDTO(Team team){
       this(team.getId(), team.getName());
    }
}
