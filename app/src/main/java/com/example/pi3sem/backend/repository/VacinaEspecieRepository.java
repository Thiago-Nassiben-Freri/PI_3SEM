package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.VacinaEspecie;
import java.util.List;

/**
 * Repository para a entidade VacinaEspecie, que representa a relação entre vacinas e espécies de animais.
 * VacinaEspecieRepository
 * List<VacinaEspecie> findByVacina_IdVacina(Integer idVacina): Método para buscar os vínculos de uma vacina.
 */

public interface VacinaEspecieRepository extends JpaRepository <VacinaEspecie, Long> {
    List<VacinaEspecie> findByVacina_IdVacina(Integer idVacina);
}