package br.ifsp.demo.repository;

import br.ifsp.demo.model.Fase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FaseRepository extends JpaRepository<Fase, UUID> {
}
