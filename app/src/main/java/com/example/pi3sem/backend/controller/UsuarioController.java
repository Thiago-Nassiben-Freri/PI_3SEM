package com.example.pi3sem.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.model.Usuario;
import com.example.pi3sem.backend.service.UsuarioService;

// Usuários são criados por POST /veterinarios ou POST /admins
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return service.atualizar(id, usuario);
    }

    // Corpo: {"ativo": true} ou {"ativo": false}
    @PatchMapping("/{id}/ativo")
    public Usuario alterarAtivo(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return service.alterarAtivo(id, usuario.getAtivo());
    }
}
