package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.pi3sem.backend.model.Tutor;
import java.util.List;
import java.util.Optional;

/**
 * Repository para a entidade Tutor.
 * TutorRepository
 * Optional<Tutor> findByCpf(String cpf): Método para buscar tutor por CPF.
 * List<Tutor> buscar(String termo): Método para buscar tutores por nome, CPF, email ou telefone.
 */

public interface TutorRepository extends JpaRepository<Tutor, Integer> {
    Optional<Tutor> findByCpf(String cpf);

    @Query("""
            SELECT t FROM Tutor t
            WHERE LOWER(t.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR t.cpf LIKE CONCAT('%', :termo, '%')
               OR LOWER(t.email) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR EXISTS (SELECT tel FROM Telefone tel
                          WHERE tel.tutor = t AND tel.numero LIKE CONCAT('%', :termo, '%'))
            """)
    List<Tutor> buscar(@Param("termo") String termo);
}
