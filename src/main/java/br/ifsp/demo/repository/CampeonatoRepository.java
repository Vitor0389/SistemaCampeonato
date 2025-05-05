package br.ifsp.demo.repository;

import br.ifsp.demo.model.Campeonato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampeonatoRepository extends JpaRepository<Campeonato, UUID>{
    List<Campeonato> findAllByUserId(UUID userId);


}
