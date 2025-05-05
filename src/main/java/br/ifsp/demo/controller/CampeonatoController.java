package br.ifsp.demo.controller;

import br.ifsp.demo.DTOs.CampeonatoDTO;
import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.services.CampeonatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
    @RequestMapping("/campeonatos")
    public class CampeonatoController {

        @Autowired
        private CampeonatoService campeonatoService;
        @Autowired
        private AuthenticationInfoService authService;


        @PostMapping
    public ResponseEntity<CampeonatoDTO> criarCampeonato(@RequestBody String name, List<Team> teams) {
            UUID usuarioId = authService.getAuthenticatedUserId();
            Campeonato campeonato = campeonatoService.createCampeonato(name, teams);
            return new ResponseEntity<>(new CampeonatoDTO(campeonato), HttpStatus.CREATED);
        }


    }

