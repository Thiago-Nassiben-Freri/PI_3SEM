package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.Raca;
import com.example.pi3sem.backend.repository.RacaRepository;

@Service 
public class RacaService {

    private final RacaRepository repository;
    private final EspecieService especieService;

    public RacaService(RacaRepository repository, EspecieService especieService) {
        this.repository = repository;
        this.especieService = especieService;
    }

    public List<Raca> listar() {
        return repository.findAll();
    }

    public Raca salvar(Raca raca) {
        if (raca == null) {
            throw new IllegalArgumentException("A raça não pode ser nula");
        }
        if (raca.getNome() == null || raca.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da raça é obrigatório");
        }
        if (raca.getEspecie() == null || raca.getEspecie().getIdEspecie() == null) {
            throw new IllegalArgumentException("A espécie da raça é obrigatória");
        }
        raca.setEspecie(especieService.buscarPorId(raca.getEspecie().getIdEspecie()));
        return repository.save(raca);
    }

}
