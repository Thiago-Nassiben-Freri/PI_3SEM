package com.example.pi3sem.backend.dto;

import java.util.List;

import com.example.pi3sem.backend.security.UsuarioAutenticado;

// Resposta de POST /auth/login e GET /auth/me; papeis: ADMIN e/ou VETERINARIO
public record UsuarioLogadoDTO(Integer idUsuario, String nome, String login, String email, List<String> papeis) {

    public static UsuarioLogadoDTO de(UsuarioAutenticado usuario) {
        return new UsuarioLogadoDTO(usuario.getIdUsuario(), usuario.getNome(), usuario.getUsername(),
                usuario.getEmail(), usuario.getPapeis());
    }
}
