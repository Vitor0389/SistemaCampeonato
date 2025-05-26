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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @PostMapping
    public ResponseEntity<List<TeamDTO>> criarTeam(@RequestBody List<TeamDTO> teamDTOList) {
        List<TeamDTO> savedTeamDTOs = teamDTOList.stream()
                .map(teamDTO -> {

                    Team team = new Team(teamDTO.id(), teamDTO.name());

                    Team savedTeam = teamRepository.save(team);

                    return new TeamDTO(savedTeam.getId(), savedTeam.getName());
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeamDTOs);
    }

}
