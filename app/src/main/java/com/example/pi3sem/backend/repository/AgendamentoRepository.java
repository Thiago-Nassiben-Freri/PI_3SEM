package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.StatusAgendamento;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para a entidade Agendamento.
 * AgendamentoRepository
 * List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim): Método para buscar agendamentos entre duas datas e horas.
 * List<Agendamento> findByPet_IdPetOrderByDataHoraAsc(Integer idPet): Método para buscar agendamentos de um pet.
 * List<Agendamento> filtrar(...): Método para buscar agendamentos por período, veterinário e/ou status.
 */

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Agendamento> findByPet_IdPetOrderByDataHoraAsc(Integer idPet);

    // inicio e fim são sempre informados pelo service; veterinarioId e status nulos não filtram
    @Query("""
            SELECT a FROM Agendamento a
            WHERE a.dataHora BETWEEN :inicio AND :fim
              AND (:veterinarioId IS NULL OR a.veterinario.id = :veterinarioId)
              AND (:status IS NULL OR a.status = :status)
            ORDER BY a.dataHora
            """)
    List<Agendamento> filtrar(@Param("inicio") LocalDateTime inicio,
                              @Param("fim") LocalDateTime fim,
                              @Param("veterinarioId") Integer veterinarioId,
                              @Param("status") StatusAgendamento status);
}
