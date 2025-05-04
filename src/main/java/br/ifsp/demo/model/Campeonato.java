package br.ifsp.demo.model;

import jakarta.servlet.http.Part;

import java.util.*;

public class Campeonato {
    private UUID id;
    private String name;
    private List<Team> times;
    private List<Fase> fases;
    private Fase currentFase;


    private Campeonato(String name, List<Team> teams) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.times = teams;
        this.fases = new ArrayList<>();
        crateInitialFase(times);
    }

    public static Campeonato createCampeonato(String name, List<Team> times){

        validateTeams(times);

        return new Campeonato(name, times);
    }

    private static void validateTeams(List<Team> times) {
        Set<UUID> idsVistos = new HashSet<>();
        boolean possuiDuplicado = false;

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

        if(times.isEmpty()){
            throw new IllegalArgumentException("O número de times deve ser no mínimo 2!");
        }

    }

    private void crateInitialFase(List<Team> times) {
        List<Partida> partidas = createPartidas(times);

        Fase faseInicial = new Fase("Fase Inicial", partidas);
        this.fases.add(faseInicial);
        this.currentFase = faseInicial;
    }

    private List<Partida> createPartidas(List<Team> times){
        List<Partida> partidas = new ArrayList<>();

        for (int i = 0; i < times.size(); i += 2) {
            Partida partida = new Partida(times.get(i), times.get(i + 1));
            partidas.add(partida);
        }

        return partidas;
    }

    public void registerResult(UUID id, Team team){
        Optional<Partida> partida = findPartidaByid(id);

        if(team == null) throw new IllegalArgumentException("Partidas não podem terminar empatadas, deve haver um vencedor!");

        if (partida.isEmpty()) throw new NoSuchElementException("Partida não encontrada, por favor informe um ID de partida válido!");

        if(partida.get().isFinished()){
            throw new IllegalStateException("Partida já está finalizada!");
        }

        partida.get().setWinner(team);
        createNewFase();
    }

    private void createNewFase(){

        List <Team> vencedores = currentFase.getVencedores();
        int indexAtual = this.fases.indexOf(currentFase);
        int indexProxima = indexAtual + 1;

        List <Partida> partidasNovaFase = createPartidas(vencedores);
        Fase novaFase = new Fase("Fase subsequente" ,partidasNovaFase);

        if(indexProxima < this.fases.size()){
            this.fases.set(indexProxima, novaFase);
        }
        else{
            if(vencedores.size() >= 2){
                this.fases.add(novaFase);
            }
        }
        if (isCurrentFaseFinished() && vencedores.size() > 1) {
            this.currentFase = novaFase;
        }
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
}
