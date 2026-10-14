package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Agendamento;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para a entidade Agendamento.
 * AgendamentoRepository
 * List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim): Método para buscar agendamentos entre duas datas e horas.
 */

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}