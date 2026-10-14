package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Vacina;
import java.util.List;

/**
 * Repository para a entidade Vacina.
 * VacinaRepository
 * List<Vacina> findByNomeContainingIgnoreCase(String nome): Método para buscar vacinas por nome.
 */

public interface VacinaRepository extends JpaRepository <Vacina, Integer> {
    List<Vacina> findByNomeContainingIgnoreCase(String nome);
}