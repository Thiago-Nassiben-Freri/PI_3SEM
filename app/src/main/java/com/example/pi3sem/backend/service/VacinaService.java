package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.repository.VacinaRepository;

@Service 
public class VacinaService {

    private final VacinaRepository repository;

    public VacinaService(VacinaRepository repository) {
        this.repository = repository;
    }

    public List<Vacina> listar() {
        return repository.findAll();
    }

    public Vacina salvar(Vacina vacina) {
        if (vacina == null) {
            throw new IllegalArgumentException("A vacina não pode ser nula");
        }
        if (vacina.getNome() == null || vacina.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da vacina é obrigatório");
        }
        if (vacina.getTipo() == null || vacina.getTipo().isBlank()) {
            throw new IllegalArgumentException("O tipo da vacina é obrigatório");
        } 
        if (vacina.getDosagem() == null || vacina.getDosagem().isBlank()) {
            throw new IllegalArgumentException("A dosagem da vacina é obrigatória");
        }
        if (vacina.getPeriodicidadeDias() == null) {
            throw new IllegalArgumentException("A periodicidade da vacina á obrigatória");
        }
        return repository.save(vacina);
    }

}
