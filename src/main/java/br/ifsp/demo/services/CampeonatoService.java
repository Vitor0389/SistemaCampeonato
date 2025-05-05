package br.ifsp.demo.services;

import br.ifsp.demo.DTOs.CampeonatoDTO;
import br.ifsp.demo.DTOs.FaseDTO;
import br.ifsp.demo.model.Campeonato;
import br.ifsp.demo.model.Team;
import br.ifsp.demo.repository.CampeonatoRepository;
import br.ifsp.demo.repository.FakeCampeonatoRepository;
import br.ifsp.demo.security.user.JpaUserRepository;
import br.ifsp.demo.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<FaseDTO> viewDetails(UUID id) {
        Optional<Campeonato> campeonatoOptional = repository.findById(id);

        if(campeonatoOptional.isPresent()){
            CampeonatoDTO dto = new CampeonatoDTO(campeonatoOptional.get());
            return dto.fases();
        }
        else{
            throw new NoSuchElementException("Campeonato deve existir!");
        }
    }

    public List<CampeonatoDTO> findAllCampeonatos(UUID usuarioId) {
        return repository.findAll().stream().map(CampeonatoDTO::new).toList();
    }
}
