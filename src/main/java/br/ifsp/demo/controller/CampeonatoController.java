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
            UUID userId = authService.getAuthenticatedUserId();
            Campeonato campeonato = campeonatoService.createCampeonato(name, teams, userId);
            return new ResponseEntity<>(new CampeonatoDTO(campeonato), HttpStatus.CREATED);
        }


        @GetMapping
        public ResponseEntity<List<CampeonatoDTO>> buscarCampeonatos() {
            UUID usuarioId = authService.getAuthenticatedUserId();
            List<CampeonatoDTO> campeonatos = campeonatoService.findAllCampeonatos(usuarioId);
            return new ResponseEntity<>(campeonatos, HttpStatus.OK);
        }

        /*@GetMapping("/{id}")
        public ResponseEntity<Campeonato> verDetalhesCampeonato(@PathVariable Long id) {
            String usuarioId = getUsuarioIdFromToken();
            Campeonato campeonato = campeonatoService.buscarCampeonatoPorId(id, usuarioId);
            return new ResponseEntity<>(campeonato, HttpStatus.OK);
        }

        // Deletar campeonato
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletarCampeonato(@PathVariable Long id) {
            String usuarioId = getUsuarioIdFromToken();
            campeonatoService.deletarCampeonato(id, usuarioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Registrar resultado de partida
        @PatchMapping("/{id}/partidas/{partidaId}/resultado")
        public ResponseEntity<Void> registrarResultadoPartida(
                @PathVariable Long id, @PathVariable Long partidaId, @RequestBody ResultadoRequest resultadoRequest) {
            String usuarioId = getUsuarioIdFromToken();
            campeonatoService.registrarResultado(id, partidaId, resultadoRequest, usuarioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
*/
    }

