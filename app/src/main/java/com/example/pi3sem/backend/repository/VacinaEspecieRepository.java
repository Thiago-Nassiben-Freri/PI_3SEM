package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.VacinaEspecie;

/**
 * Repository para a entidade VacinaEspecie, que representa a relação entre vacinas e espécies de animais.
 * VacinaEspecieRepository
 */

public interface VacinaEspecieRepository extends JpaRepository <VacinaEspecie, Integer> {
}