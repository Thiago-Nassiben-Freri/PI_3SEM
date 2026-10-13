package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

}