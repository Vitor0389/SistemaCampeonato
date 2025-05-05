package br.ifsp.demo.repository;

import br.ifsp.demo.model.Campeonato;

import java.util.*;
import java.util.function.Function;

public class FakeCampeonatoRepository implements CampeonatoRepository{
    private final Map<UUID, Campeonato> banco = new HashMap<>();


    @Override
    public Campeonato save(Campeonato campeonato) {
        banco.put(campeonato.getId(), campeonato);
        return campeonato;
    }

    @Override
    public Optional<Campeonato> findById(UUID id) {
        return Optional.ofNullable(banco.get(id));
    }

    @Override
    public List<Campeonato> findAll() {
        return new ArrayList<>(banco.values());
    }

    @Override
    public void deleteById(UUID id) {
        banco.remove(id);
    }
}
