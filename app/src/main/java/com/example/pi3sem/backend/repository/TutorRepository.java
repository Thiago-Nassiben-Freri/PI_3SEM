package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Tutor;
import java.util.Optional;

/**
 * Repository para a entidade Tutor.
 * TutorRepository
 * Optional<Tutor> findByCpf(String cpf): Método para buscar tutor por CPF.
 */

public interface TutorRepository extends JpaRepository<Tutor, Integer> {
    Optional<Tutor> findByCpf(String cpf);
}
