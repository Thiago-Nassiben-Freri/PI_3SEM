package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Especie;
import java.util.List;

/**
 * Repository para a entidade Especie.
 * EspecieRepository
 * List<Especie> findByNomeContainingIgnoreCase(String nome) - Método para buscar espécies pelo nome, ignorando maiúsculas e minúsculas.
 */

public interface EspecieRepository extends JpaRepository<Especie, Integer> {
    List<Especie> findByNomeContainingIgnoreCase(String nome);
}