package br.ifsp.demo.repository;

import br.ifsp.demo.model.Campeonato;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.function.Function;

public class FakeCampeonatoRepository implements CampeonatoRepository{
    private final Map<UUID, Campeonato> banco = new HashMap<>();


    public Campeonato save(Campeonato campeonato) {
        banco.put(campeonato.getId(), campeonato);
        return campeonato;
    }

    public Optional<Campeonato> findById(UUID id) {
        return Optional.ofNullable(banco.get(id));
    }


    public List<Campeonato> findAll() {
        return new ArrayList<>(banco.values());
    }


    public long count() {
        return banco.size();
    }


    public void deleteById(UUID id) {
        banco.remove(id);
    }

}


