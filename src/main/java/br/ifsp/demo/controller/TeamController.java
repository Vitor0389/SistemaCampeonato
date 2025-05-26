package br.ifsp.demo.controller;

import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @PostMapping
    public ResponseEntity<TeamDTO> criarTeam(@RequestBody TeamDTO teamDTO) {
        Team team = new Team(teamDTO.id(), teamDTO.name());
        teamRepository.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TeamDTO(team));
    }
}
