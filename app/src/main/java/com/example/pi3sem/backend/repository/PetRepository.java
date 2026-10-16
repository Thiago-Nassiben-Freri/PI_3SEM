package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.pi3sem.backend.model.Pet;
import java.util.List;

/**
 * Repository para a entidade Pet.
 * PetRepository
 * List<Pet> findByTutor_IdTutor(int idTutor): Método para buscar pets por ID do tutor.
 * List<Pet> filtrar(String nome, Integer especieId, Integer tutorId): Método para buscar pets por nome, espécie e/ou tutor.
 */

public interface PetRepository extends JpaRepository<Pet, Integer> {
    List<Pet> findByTutor_IdTutor(int idTutor);

    // nome vazio ("") não filtra; especieId/tutorId nulos não filtram
    @Query("""
            SELECT p FROM Pet p
            WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
              AND (:especieId IS NULL OR p.raca.especie.idEspecie = :especieId)
              AND (:tutorId IS NULL OR p.tutor.idTutor = :tutorId)
            """)
    List<Pet> filtrar(@Param("nome") String nome,
                      @Param("especieId") Integer especieId,
                      @Param("tutorId") Integer tutorId);
}