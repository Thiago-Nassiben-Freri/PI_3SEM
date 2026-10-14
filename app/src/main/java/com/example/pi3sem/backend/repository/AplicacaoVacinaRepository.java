package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.AplicacaoVacina;
import java.util.List;

/**
 * Repository para a entidade AplicacaoVacina.
 * AplicacaoVacinaRepository
 * List<AplicacaoVacina> findByPet_IdPet(Integer idPet): Método para buscar aplicações de vacina por ID do pet.
 */

public interface AplicacaoVacinaRepository extends JpaRepository<AplicacaoVacina, Integer> {
    List<AplicacaoVacina> findByPet_IdPet(Integer idPet);
}