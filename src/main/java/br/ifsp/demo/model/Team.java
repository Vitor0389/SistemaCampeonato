package br.ifsp.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Team {
    @Id
    private UUID id;
    private String name;

    public Team(UUID id, String name){
        this.id = id;
        this.name = name;
    }

    protected Team() {

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
