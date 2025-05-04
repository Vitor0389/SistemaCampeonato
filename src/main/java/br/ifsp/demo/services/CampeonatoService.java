package br.ifsp.demo.services;

import br.ifsp.demo.DTOs.CampeonatoDTO;
import br.ifsp.demo.DTOs.FaseDTO;
import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.CampeonatoRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class CampeonatoService {

    private final CampeonatoRepository repository;

    public CampeonatoService(CampeonatoRepository repository) {
        this.repository = repository;
    }

    public Campeonato createCampeonato(String name, List<Team> teams) {
        return Campeonato.createCampeonato(name, teams);
    }

    public List<FaseDTO> viewDetails(UUID id) {
        Optional<Campeonato> campeonatoOptional = repository.findById(id);

        if(campeonatoOptional.isPresent()){
            CampeonatoDTO dto = new CampeonatoDTO(campeonatoOptional.get());
            return dto.fases();
        }
        else{
            throw new NoSuchElementException("Campeonato deve existir!");
        }
    }
}
