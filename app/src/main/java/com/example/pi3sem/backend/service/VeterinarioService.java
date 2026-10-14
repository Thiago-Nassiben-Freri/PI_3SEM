package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.Veterinario;
import com.example.pi3sem.backend.repository.VeterinarioRepository;

@Service 
public class VeterinarioService {

    private final VeterinarioRepository repository;

    public VeterinarioService(VeterinarioRepository repository) {
        this.repository = repository;
    }

    public List<Veterinario> listar() {
        return repository.findAll();
    }

    public Veterinario salvar(Veterinario veterinario) {
        if (veterinario == null) {
            throw new IllegalArgumentException("O veterinario não pode ser nulo");
        }
        if (veterinario.getCrmv() == null) {
            throw new IllegalArgumentException("O CRMV do veterinário é obrigatório");
        }
        if (veterinario.getTelefone() == null || veterinario.getTelefone().isBlank()) {
            throw new IllegalArgumentException("O Telefone do veterinário é obrigatório");
        }
        if (veterinario.getUsuario() == null || veterinario.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("O usuário é obrigatório");
        }
        return repository.save(veterinario);
    }

}
