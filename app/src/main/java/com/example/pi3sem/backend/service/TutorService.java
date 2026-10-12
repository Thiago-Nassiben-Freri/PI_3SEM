package com.example.pi3sem.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.pi3sem.backend.model.Tutor;
import com.example.pi3sem.backend.repository.TutorRepository;

@Service
public class TutorService {

    private final TutorRepository repository;

    public TutorService(TutorRepository repository) {
        this.repository = repository;
    }

    public List<Tutor> listar() {
        return repository.findAll();
    }

    public Tutor salvar(Tutor tutor) {
        if (tutor.getCpf() == null || tutor.getCpf().isBlank()) {
            throw new IllegalArgumentException("O CPF do tutor é obrigatório");
        }
        if (tutor.getNome() == null || tutor.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do tutor é obrigatório");
        }
        if (tutor.getEmail() == null || tutor.getEmail().isBlank()) {
            throw new IllegalArgumentException("O email do tutor é obrigatório");
        }
        if (tutor.getCep() == null || tutor.getCep().isBlank()) {
            throw new IllegalArgumentException("O CEP do tutor é obrigatório");
        }
        if (tutor.getNumero() == null || tutor.getNumero().isBlank()) {
            throw new IllegalArgumentException("O número do endereço do tutor é obrigatório");
        }
        return repository.save(tutor);
    }

}
