package com.example.pi3sem.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.pi3sem.backend.security.UsuarioAutenticado;

class AplicacaoVacinaControllerVisibilityTest {

    @Test
    void adminDeveVerTodasAsAplicacoes() {
        UsuarioAutenticado admin = new UsuarioAutenticado(99, "Admin", "admin", "admin@teste.com", "123", true,
                List.of("ADMIN"));

        assertNull(AplicacaoVacinaController.aplicarVisibilidadePorPerfil(null, admin));
        assertEquals(12, AplicacaoVacinaController.aplicarVisibilidadePorPerfil(12, admin));
    }

    @Test
    void veterinarioDeveVerApenasSuasAplicacoes() {
        UsuarioAutenticado veterinario = new UsuarioAutenticado(7, "Dr. House", "vet", "vet@teste.com", "123", true,
                List.of("VETERINARIO"));

        assertEquals(7, AplicacaoVacinaController.aplicarVisibilidadePorPerfil(null, veterinario));
        assertEquals(7, AplicacaoVacinaController.aplicarVisibilidadePorPerfil(12, veterinario));
    }
}
