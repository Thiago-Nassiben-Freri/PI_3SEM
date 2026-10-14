package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.Telefone;
import com.example.pi3sem.backend.repository.TelefoneRepository;

@Service 
public class TelefoneService {

    private final TelefoneRepository repository;

    public TelefoneService(TelefoneRepository repository) {
        this.repository = repository;
    }

    public List<Telefone> listar() {
        return repository.findAll();
    }

    public Telefone salvar(Telefone telefone) {
        if (telefone == null) {
            throw new IllegalArgumentException("O telefone não pode ser nulo");
        }
        if (telefone.getNumero() == null || telefone.getNumero().isBlank()) {
            throw new IllegalArgumentException("O número do telefone é obrigatório");
        }
        if (telefone.getTutor() == null || telefone.getTutor().getIdTutor() == null) {
            throw new IllegalArgumentException("O tutor do pet é obrigatório");
        }
        return repository.save(telefone);
    }

}
