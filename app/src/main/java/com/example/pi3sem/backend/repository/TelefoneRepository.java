package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Telefone;
import java.util.List;

/**
 * Repository para a entidade Telefone.
 * TelefoneRepository
 * List<Telefone> findByTutor_Id(Integer idTutor): Método para buscar telefones por ID do tutor.
 */

public interface TelefoneRepository extends JpaRepository<Telefone, Integer> {
    List<Telefone> findByTutor_Id(Integer idTutor);
}