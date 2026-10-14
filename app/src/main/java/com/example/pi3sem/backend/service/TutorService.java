package com.example.pi3sem.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
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

    public Tutor buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor", id));
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
        validarCpfUnico(tutor.getCpf(), null);
        return repository.save(tutor);
    }

    // idAtual: id do próprio tutor em uma atualização (null no cadastro)
    private void validarCpfUnico(String cpf, Integer idAtual) {
        repository.findByCpf(cpf)
                .filter(existente -> !existente.getIdTutor().equals(idAtual))
                .ifPresent(existente -> {
                    throw new IllegalArgumentException("Já existe um tutor cadastrado com este CPF");
                });
    }

}
