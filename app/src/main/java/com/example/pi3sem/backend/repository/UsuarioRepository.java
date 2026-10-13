package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}