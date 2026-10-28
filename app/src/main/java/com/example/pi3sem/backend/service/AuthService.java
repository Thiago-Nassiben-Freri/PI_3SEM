package com.example.pi3sem.backend.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.dto.LoginDTO;
import com.example.pi3sem.backend.dto.UsuarioLogadoDTO;
import com.example.pi3sem.backend.exception.CredenciaisInvalidasException;
import com.example.pi3sem.backend.security.PetvaxUserDetailsService;
import com.example.pi3sem.backend.security.UsuarioAutenticado;

/**
 * Login para a tela de login do desktop. A API usa HTTP Basic: depois de validar aqui,
 * o desktop guarda login e senha e envia no cabeçalho Authorization de cada requisição.
 */
@Service
public class AuthService {

    private final PetvaxUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(PetvaxUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioLogadoDTO login(LoginDTO dados) {
        if (dados == null || dados.login() == null || dados.login().isBlank()
                || dados.senha() == null || dados.senha().isBlank()) {
            throw new IllegalArgumentException("Login e senha são obrigatórios");
        }
        UsuarioAutenticado usuario;
        try {
            usuario = userDetailsService.loadUserByUsername(dados.login());
        } catch (UsernameNotFoundException ex) {
            throw new CredenciaisInvalidasException("Login ou senha inválidos");
        }
        if (!passwordEncoder.matches(dados.senha(), usuario.getPassword())) {
            throw new CredenciaisInvalidasException("Login ou senha inválidos");
        }
        if (!usuario.isEnabled()) {
            throw new CredenciaisInvalidasException("Usuário inativo");
        }
        return UsuarioLogadoDTO.de(usuario);
    }
}
