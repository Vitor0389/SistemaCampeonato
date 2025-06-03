package br.ifsp.demo.services;

import br.ifsp.demo.DTOs.CampeonatoDTO;
import br.ifsp.demo.DTOs.FaseDTO;
import br.ifsp.demo.DTOs.TeamDTO;
import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.CampeonatoRepository;
import br.ifsp.demo.repository.FakeCampeonatoRepository;
import br.ifsp.demo.security.user.JpaUserRepository;
import br.ifsp.demo.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CampeonatoService {

    @Autowired
    private final CampeonatoRepository repository;
    @Autowired
    private JpaUserRepository userRepository;

    public CampeonatoService(CampeonatoRepository repository, JpaUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }


    public Campeonato createCampeonato(String name, List<Team> teams, UUID userID) {
        Campeonato campeonato = Campeonato.createCampeonato(name, teams);
        User user = userRepository.getReferenceById(userID);
        campeonato.setUser(user);
        repository.save(campeonato);
        return campeonato;
    }

    public List<FaseDTO> viewDetails(UUID id, UUID userID) {
        Optional<Campeonato> campeonatoOptional = repository.findByIdAndUserId(id, userID);

        if (campeonatoOptional.isPresent()) {
            CampeonatoDTO dto = new CampeonatoDTO(campeonatoOptional.get());
            return dto.fases();
        } else {
            throw new NoSuchElementException("Campeonato deve existir!");
        }
    }

    public List<CampeonatoDTO> findAllCampeonatos(UUID usuarioId) {
        return repository.findAllByUserId(usuarioId)
                .stream()
                .map(CampeonatoDTO::new)
                .toList();
    }

    public CampeonatoDTO findByIdAndUserId(UUID campeonatoID, UUID user) {
        Optional<Campeonato> optionalCampeonato = repository.findByIdAndUserId(campeonatoID, user);
        if (optionalCampeonato.isPresent()) {
            return new CampeonatoDTO(optionalCampeonato.get());
        } else {
            throw new NoSuchElementException("Campeonato não existe");
        }
    }

    public void deleteCampeonato(UUID id, UUID user) {
        Optional<Campeonato> campeonatoOptional = repository.findByIdAndUserId(id, user);
        if (campeonatoOptional.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new NoSuchElementException("Campeonato que está tentando deletar não existe!");
        }
    }

    @Transactional
    public void registerResult(UUID campId, UUID partidaId, TeamDTO teamDTO, UUID userId){
        Optional<Campeonato> campeonato = repository.findByIdAndUserId(campId, userId);
        if(campeonato.isPresent()){
            Team team = new Team(teamDTO.id(), teamDTO.name());
            campeonato.get().registerResult(partidaId, team);
        }
        else{
            throw new NoSuchElementException("Campeonato não encontrado");
        }
    }
}