package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.VacinaEspecie;
import java.util.List;

/**
 * Repository para a entidade VacinaEspecie, que representa a relação entre vacinas e espécies de animais.
 * VacinaEspecieRepository
 * List<VacinaEspecie> findByVacina_IdVacina(Integer idVacina): Método para buscar todas as relações de VacinaEspecie por ID de Vacina.
 * List<VacinaEspecie> findByEspecie_IdEspecie(Integer idEspecie): Método para buscar todas as relações de VacinaEspecie por ID de Espécie.
 */

public interface VacinaEspecieRepository extends JpaRepository <VacinaEspecie, Integer> {
    List <VacinaEspecie> findByVacina_IdVacina(Integer idVacina);
    List <VacinaEspecie> findByEspecie_IdEspecie(Integer idEspecie);
}