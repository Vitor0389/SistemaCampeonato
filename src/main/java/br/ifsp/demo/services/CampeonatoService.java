package br.ifsp.demo.services;

import br.ifsp.demo.DTOs.CampeonatoDTO;
import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.CampeonatoRepository;

import java.util.List;
import java.util.UUID;

public class CampeonatoService {

    private final CampeonatoRepository repository;

    public CampeonatoService(CampeonatoRepository repository) {
        this.repository = repository;
    }

    public Campeonato createCampeonato(String name, List<Team> teams) {
        return Campeonato.createCampeonato(name, teams);
    }

    public CampeonatoDTO viewDetails(UUID id) {
        CampeonatoDTO dto = new CampeonatoDTO(repository.findById(id));
        return dto;
    }
}
