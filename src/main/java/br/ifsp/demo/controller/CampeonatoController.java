package br.ifsp.demo.controller;

import br.ifsp.demo.DTOs.CampeonatoDTO;
import br.ifsp.demo.DTOs.CampeonatoRequestDTO;
import br.ifsp.demo.DTOs.FaseDTO;
import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Fase;
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
    @RequestMapping("/api/v1/campeonatos")
    public class CampeonatoController {

        @Autowired
        private CampeonatoService campeonatoService;
        @Autowired
        private AuthenticationInfoService authService;

        @PostMapping
        public ResponseEntity<CampeonatoDTO> criarCampeonato(@RequestBody CampeonatoRequestDTO request) {
            UUID userId = authService.getAuthenticatedUserId();
            List<Team> teams = request.teams().stream().map(dto -> new Team(dto.id(), dto.name()))
                    .toList();
            Campeonato campeonato = campeonatoService.createCampeonato(request.name(), teams, userId);
            return new ResponseEntity<>(new CampeonatoDTO(campeonato), HttpStatus.CREATED);
        }


        @GetMapping
        public ResponseEntity<List<CampeonatoDTO>> buscarCampeonatos() {
            UUID usuarioId = authService.getAuthenticatedUserId();
            List<CampeonatoDTO> campeonatos = campeonatoService.findAllCampeonatos(usuarioId);
            return new ResponseEntity<>(campeonatos, HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<List<FaseDTO>> verDetalhesCampeonato(@PathVariable UUID id) {
            UUID userId = authService.getAuthenticatedUserId();
            List<FaseDTO> fases = campeonatoService.viewDetails(id, userId);
            return new ResponseEntity<>(fases, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletarCampeonato(@PathVariable UUID id) {
            UUID usuarioId = authService.getAuthenticatedUserId();
            campeonatoService.deleteCampeonato(id, usuarioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @PatchMapping("/{campId}/resultado/partida/{partidaId}")
        public ResponseEntity<Void> registrarResultadoPartida(
                @PathVariable UUID campId, @PathVariable UUID partidaId, @RequestBody TeamDTO teamDTO) {
            UUID usuarioId = authService.getAuthenticatedUserId();
            campeonatoService.registerResult(campId, partidaId, teamDTO, usuarioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

