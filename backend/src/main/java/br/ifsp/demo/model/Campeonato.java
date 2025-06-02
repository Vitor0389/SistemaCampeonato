package br.ifsp.demo.model;

import br.ifsp.demo.security.user.User;
import jakarta.persistence.*;
import jakarta.servlet.http.Part;

import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Campeonato {
    @Id
    private UUID id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "campeonato_id")
    private List<Team> times;
    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fase> fases;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fase_atual_id")
    private Fase currentFase;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    private Campeonato(String name, List<Team> teams) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.times = teams;
        this.fases = new ArrayList<>();
        crateInitialFase(times);
    }

    protected Campeonato() {

    }


    public static Campeonato createCampeonato(String name, List<Team> times){

        validateTeams(times);

        return new Campeonato(name, times);
    }

    private static void validateTeams(List<Team> times) {
        Set<UUID> idsVistos = new HashSet<>();
        boolean possuiDuplicado = false;

        if(times == null){
            throw new NullPointerException("A lista de times não pode ser nula!");
        }

        if(times.size() < 2 || times.size() > 32)
            throw new IllegalArgumentException("A quantidade de times inválida!");

        if((times.size() & (times.size() - 1))!= 0)
            throw new IllegalArgumentException("O número de times deve ser potência de 2!");

        for(Team team : times){

            if(!idsVistos.add(team.getId())){
                possuiDuplicado = true;
            }

        }

        if(possuiDuplicado){
            throw new IllegalStateException("Dois times não podem ter o mesmo ID!");
        }

    }

    private void crateInitialFase(List<Team> times) {
        List<Partida> partidas = createPartidas(times);

        Fase faseInicial = new Fase("Fase Inicial", partidas);
        faseInicial.setCampeonato(this);
        setFaseEmPartidas(partidas, faseInicial);

        this.fases.add(faseInicial);
        this.currentFase = faseInicial;
    }

    private List<Partida> createPartidas(List<Team> times){
        List<Partida> partidas = new ArrayList<>();

        for (int i = 0; i < times.size(); i += 2) {
            Partida partida = new Partida(times.get(i), times.get(i + 1));
            partida.setChave(i / 2);
            partidas.add(partida);
        }

        return partidas;
    }

    public void registerResult(UUID id, Team team) {
        Optional<Partida> partida = findPartidaByid(id);

        if (team == null) throw new IllegalArgumentException("Partidas não podem terminar empatadas, deve haver um vencedor!");
        if (partida.isEmpty()) throw new NoSuchElementException("Partida não encontrada, por favor informe um ID de partida válido!");
        if (partida.get().isFinished()) throw new IllegalStateException("Partida já está finalizada!");

        Partida partidaFinal = partida.get();
        partidaFinal.setWinner(team);
        partidaFinal.getFase().addWinner(team);

        // Só tenta criar nova fase se a fase atual terminou
        if (isCurrentFaseFinished() && currentFase.getVencedores().size() >= 2) {
            createNewFase();
        }
    }

    private void createNewFase() {

        List<Partida> partidasAtuais = currentFase.getPartidas()
                .stream()
                .sorted(Comparator.comparingInt(Partida::getChave))
                .toList();

        List<Team> vencedores = partidasAtuais.stream()
                .map(Partida::getWinner)
                .toList();

        List<Partida> novasPartidas = new ArrayList<>();
        for (int i = 0; i < vencedores.size(); i += 2) {
            Team teamA = vencedores.get(i);
            Team teamB = vencedores.get(i + 1);
            Partida nova = new Partida(teamA, teamB);
            nova.setChave(i / 2);
            novasPartidas.add(nova);
        }

        Fase novaFase = new Fase("Fase " + (fases.size() + 1), novasPartidas);
        novaFase.setCampeonato(this);
        setFaseEmPartidas(novasPartidas, novaFase);
        this.fases.add(novaFase);
        this.currentFase = novaFase;
    }
    private void setFaseEmPartidas(List<Partida> partidas, Fase fase) {
        partidas.stream().forEach(partida -> partida.setFase(fase));
    }

    private boolean isCurrentFaseFinished(){
        return currentFase.getPartidas().stream().allMatch(Partida::isFinished);

    };

    private Optional<Partida> findPartidaByid(UUID id){
       return fases.stream()
                .flatMap(fase -> fase.getPartidas().stream())
                .filter(partida -> partida.getId().equals(id))
                .findFirst();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Team> getTimes() {
        return times;
    }

    public List<Fase> getFasesList() {
        return fases;
    }

    public Fase getCurrentFase() {
        return currentFase;
    }

    public Team getWinner() {

        if(isCurrentFaseFinished() && currentFase.getPartidas().size() == 1){
            return currentFase.getVencedores().getFirst();
        }
        else{
            throw new NoSuchElementException("Campeonato ainda não terminou!");
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
