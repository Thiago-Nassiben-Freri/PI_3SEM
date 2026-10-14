package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Veterinario;

/**
 * Repository para a entidade Veterinario.
 * VeterinarioRepository
 */

public interface VeterinarioRepository extends JpaRepository <Veterinario, Integer> {
}