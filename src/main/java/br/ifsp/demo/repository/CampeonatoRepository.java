package br.ifsp.demo.repository;

import br.ifsp.demo.model.Campeonato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampeonatoRepository{
    Campeonato save(Campeonato campeonato);

    Optional<Campeonato> findById(UUID id);

    List<Campeonato> findAll();

    void deleteById(UUID id);
}
