package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
import com.example.pi3sem.backend.model.Usuario;
import com.example.pi3sem.backend.repository.AdminRepository;
import com.example.pi3sem.backend.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        validar(usuario);
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new IllegalArgumentException("A senha de usuário é obrigatória");
        }
        validarUnicidade(usuario.getLogin(), usuario.getEmail(), null);
        usuario.setIdUsuario(null);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        if (usuario.getAtivo() == null) {
            usuario.setAtivo(true);
        }
        return repository.save(usuario);
    }

    // Senha é opcional: se vier vazia, a atual é mantida
    public Usuario atualizar(Integer id, Usuario dados) {
        Usuario usuario = buscarPorId(id);
        if (dados == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        validar(dados);
        validarUnicidade(dados.getLogin(), dados.getEmail(), id);
        usuario.setNome(dados.getNome());
        usuario.setLogin(dados.getLogin());
        usuario.setEmail(dados.getEmail());
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
        }
        return repository.save(usuario);
    }

    // Desativar em vez de excluir: o usuário continua ligado ao histórico
    // (agendamentos, aplicações), mas deixa de poder usar o sistema
    public Usuario alterarAtivo(Integer id, Boolean ativo) {
        if (ativo == null) {
            throw new IllegalArgumentException("O campo ativo é obrigatório");
        }
        Usuario usuario = buscarPorId(id);
        if (!ativo && Boolean.TRUE.equals(usuario.getAtivo())
                && adminRepository.existsById(id)
                && adminRepository.countByUsuario_AtivoTrue() <= 1) {
            throw new IllegalArgumentException("Não é possível desativar o último administrador ativo");
        }
        usuario.setAtivo(ativo);
        return repository.save(usuario);
    }

    // Usado no cadastro de veterinário e admin: com idUsuario reaproveita
    // um usuário existente, sem idUsuario cria um novo
    public Usuario buscarOuCriar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("O usuário é obrigatório");
        }
        if (usuario.getIdUsuario() != null) {
            return buscarPorId(usuario.getIdUsuario());
        }
        return salvar(usuario);
    }

    private void validar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("O Nome de usuário é obrigatório");
        }
        if (usuario.getLogin() == null || usuario.getLogin().isBlank()) {
            throw new IllegalArgumentException("O Login de usuário é obrigatório");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("O email de usuário é obrigatório");
        }
    }

    // idAtual: id do próprio usuário em uma atualização (null no cadastro)
    private void validarUnicidade(String login, String email, Integer idAtual) {
        repository.findByLogin(login)
                .filter(existente -> !existente.getIdUsuario().equals(idAtual))
                .ifPresent(existente -> {
                    throw new IllegalArgumentException("Já existe um usuário com este login");
                });
        repository.findByEmail(email)
                .filter(existente -> !existente.getIdUsuario().equals(idAtual))
                .ifPresent(existente -> {
                    throw new IllegalArgumentException("Já existe um usuário com este email");
                });
    }

}
