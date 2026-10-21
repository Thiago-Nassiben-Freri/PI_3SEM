package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Veterinario;
import java.util.Optional;

/**
 * Repository para a entidade Veterinario.
 * VeterinarioRepository
 * Optional<Veterinario> findByCrmv(Integer crmv): Método para buscar veterinário por CRMV.
 */

public interface VeterinarioRepository extends JpaRepository <Veterinario, Integer> {
    Optional<Veterinario> findByCrmv(Integer crmv);
}
