package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Usuario;
import java.util.Optional;

/**
 * Repository para a entidade Usuario.
 * UsuarioRepository
 * Optional<Usuario> findByLogin(String login): Método para buscar usuário por login.
 * Optional<Usuario> findByEmail(String email): Método para buscar usuário por email.
 */

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> findByEmail(String email);
}