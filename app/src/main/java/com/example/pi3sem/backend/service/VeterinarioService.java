package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pi3sem.backend.model.Usuario;
import com.example.pi3sem.backend.model.Veterinario;
import com.example.pi3sem.backend.repository.VeterinarioRepository;

@Service
public class VeterinarioService {

    private final VeterinarioRepository repository;
    private final UsuarioService usuarioService;

    public VeterinarioService(VeterinarioRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public List<Veterinario> listar() {
        return repository.findAll();
    }

    // Cria o usuário (ou reaproveita um existente pelo idUsuario) e o veterinário
    // na mesma transação: se algo falhar, nenhum dos dois é gravado
    @Transactional
    public Veterinario salvar(Veterinario veterinario) {
        validar(veterinario);
        if (veterinario.getUsuario() == null) {
            throw new IllegalArgumentException("O usuário é obrigatório");
        }
        validarCrmvUnico(veterinario.getCrmv(), null);
        Usuario usuario = usuarioService.buscarOuCriar(veterinario.getUsuario());
        if (repository.existsById(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("Este usuário já está cadastrado como veterinário");
        }
        veterinario.setId(null);
        veterinario.setUsuario(usuario);
        return repository.save(veterinario);
    }

    private void validar(Veterinario veterinario) {
        if (veterinario == null) {
            throw new IllegalArgumentException("O veterinario não pode ser nulo");
        }
        if (veterinario.getCrmv() == null) {
            throw new IllegalArgumentException("O CRMV do veterinário é obrigatório");
        }
        if (veterinario.getTelefone() == null || veterinario.getTelefone().isBlank()) {
            throw new IllegalArgumentException("O Telefone do veterinário é obrigatório");
        }
        if (veterinario.getTelefone().length() > 15) {
            throw new IllegalArgumentException("O Telefone do veterinário deve ter no máximo 15 caracteres");
        }
    }

    // idAtual: id do próprio veterinário em uma atualização (null no cadastro)
    private void validarCrmvUnico(Integer crmv, Integer idAtual) {
        repository.findByCrmv(crmv)
                .filter(existente -> !existente.getId().equals(idAtual))
                .ifPresent(existente -> {
                    throw new IllegalArgumentException("Já existe um veterinário com este CRMV");
                });
    }

}
