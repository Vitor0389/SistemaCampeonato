package br.ifsp.demo.model;

import java.util.UUID;

public class Team {
    private UUID id;
    private String name;

    public Team(UUID id, String name){
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
