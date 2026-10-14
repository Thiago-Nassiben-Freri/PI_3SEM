package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Pet;
import java.util.List;

/**
 * Repository para a entidade Pet.
 * PetRepository
 * List<Pet> findByTutor_IdTutor(int idTutor): Método para buscar pets por ID do tutor.
 */

public interface PetRepository extends JpaRepository<Pet, Integer> {
    List<Pet> findByTutor_IdTutor(int idTutor);
}