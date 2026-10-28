package com.example.pi3sem.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.dto.LoginDTO;
import com.example.pi3sem.backend.dto.UsuarioLogadoDTO;
import com.example.pi3sem.backend.security.UsuarioAutenticado;
import com.example.pi3sem.backend.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    // Rota pública. Corpo: {"login": "...", "senha": "..."}
    @PostMapping("/login")
    public UsuarioLogadoDTO login(@RequestBody LoginDTO dados) {
        return service.login(dados);
    }

    // Dados do usuário que está fazendo a requisição (HTTP Basic)
    @GetMapping("/me")
    public UsuarioLogadoDTO me(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return UsuarioLogadoDTO.de(usuario);
    }
}
