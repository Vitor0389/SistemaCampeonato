package br.ifsp.demo.services;

import br.ifsp.demo.model.Campeonato;

public class CampeonatoService {
    public boolean createCampeonato(List<Times> times) {
        Campeonato campeonato = new Campeonato(times);
    }
}
