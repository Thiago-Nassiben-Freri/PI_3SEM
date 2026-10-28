package com.example.pi3sem.backend.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Usuário logado, como o Spring Security enxerga. Papéis:
 * ROLE_ADMIN (existe registro em "admin") e ROLE_VETERINARIO (existe registro em "veterinario").
 */
public class UsuarioAutenticado implements UserDetails {

    private final Integer idUsuario;
    private final String nome;
    private final String login;
    private final String email;
    private final String senha;
    private final boolean ativo;
    private final List<String> papeis;

    public UsuarioAutenticado(Integer idUsuario, String nome, String login, String email, String senha,
            boolean ativo, List<String> papeis) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.login = login;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.papeis = papeis;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    // Papéis sem o prefixo ROLE_ (ex.: ADMIN, VETERINARIO)
    public List<String> getPapeis() {
        return papeis;
    }

    public boolean isAdmin() {
        return papeis.contains("ADMIN");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return papeis.stream().map(papel -> new SimpleGrantedAuthority("ROLE_" + papel)).toList();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
