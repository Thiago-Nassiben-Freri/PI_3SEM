package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Notificacao;
import java.util.List;

/**
 * Repository para a entidade Notificacao.
 * NotificacaoRepository
 * List<Notificacao> findByTutor_IdTutor(Integer idTutor): Método para buscar notificações por ID do tutor. 
 */

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByTutor_IdTutor(Integer idTutor);
}