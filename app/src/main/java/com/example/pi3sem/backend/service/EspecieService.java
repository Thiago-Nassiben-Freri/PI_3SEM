package com.example.pi3sem.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.pi3sem.backend.model.Especie;
import com.example.pi3sem.backend.repository.EspecieRepository;

@Service
public class EspecieService {

    private final EspecieRepository repository;

    public EspecieService(EspecieRepository repository) {
        this.repository = repository;
    }

    public List<Especie> listar() {
        return repository.findAll();
    }

    public Especie salvar(Especie especie) {
        if (especie.getNome() == null || especie.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da espécie é obrigatório");
        }
        return repository.save(especie);
    }
}

