package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Veterinario;
import java.util.List;

/**
 * Repository para a entidade Veterinario.
 * VeterinarioRepository
 * List<Veterinario> findByUsuario_IdUsuario(Integer idUsuario): Método para buscar veterinário por ID de Usuário.
 */

public interface VeterinarioRepository extends JpaRepository <Veterinario, Integer> {
    List <Veterinario> findByUsuario_IdUsuario(Integer idUsuario);
}