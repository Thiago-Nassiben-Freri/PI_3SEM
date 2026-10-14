package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Raca;
import java.util.List;

/**
 * Repository para a entidade Raca.
 * RacaRepository
 * List<Raca> findByNomeContainingIgnoreCase(Integer idEspecie): Método para buscar raças por ID da espécie.
 */

public interface RacaRepository extends JpaRepository<Raca, Integer> {
    List<Raca> findByNomeContainingIgnoreCase(Integer idEspecie);
}