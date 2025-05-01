package br.ifsp.demo.services;

import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;

import java.util.List;

public class CampeonatoService {
    public Campeonato createCampeonato(List<Team> teams) {
         return new Campeonato(teams);
    }
}
