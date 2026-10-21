package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.pi3sem.backend.model.CanalNotificacao;
import com.example.pi3sem.backend.model.Notificacao;
import com.example.pi3sem.backend.model.StatusNotificacao;
import com.example.pi3sem.backend.model.TipoNotificacao;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository para a entidade Notificacao.
 * NotificacaoRepository
 * List<Notificacao> findByTutor_IdTutor(Integer idTutor): Método para buscar notificações por ID do tutor.
 * List<Notificacao> filtrar(...): Método para buscar notificações por status, tipo, canal, leitura, tutor e/ou período.
 * boolean existsByAplicacaoVacina_IdAplicacaoVacinaAndTipo(...): Verifica se já existe lembrete para a aplicação.
 * boolean existsByAgendamento_IdAgendamentoAndTipo(...): Verifica se já existe lembrete para o agendamento.
 * long contarNaoLidas(): Conta as notificações ainda não lidas.
 */

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByTutor_IdTutor(Integer idTutor);

    // inicio e fim são sempre informados pelo service; os demais filtros nulos não filtram.
    // lida nula no banco (registros antigos) é tratada como false
    @Query("""
            SELECT n FROM Notificacao n
            WHERE n.dataEnvio BETWEEN :inicio AND :fim
              AND (:status IS NULL OR n.status = :status)
              AND (:tipo IS NULL OR n.tipo = :tipo)
              AND (:canal IS NULL OR n.canal = :canal)
              AND (:lida IS NULL OR COALESCE(n.lida, FALSE) = :lida)
              AND (:tutorId IS NULL OR n.tutor.idTutor = :tutorId)
            ORDER BY n.dataEnvio DESC, n.idNotificacao DESC
            """)
    List<Notificacao> filtrar(@Param("inicio") LocalDate inicio,
                              @Param("fim") LocalDate fim,
                              @Param("status") StatusNotificacao status,
                              @Param("tipo") TipoNotificacao tipo,
                              @Param("canal") CanalNotificacao canal,
                              @Param("lida") Boolean lida,
                              @Param("tutorId") Integer tutorId);

    boolean existsByAplicacaoVacina_IdAplicacaoVacinaAndTipo(Integer idAplicacaoVacina, TipoNotificacao tipo);

    boolean existsByAgendamento_IdAgendamentoAndTipo(Integer idAgendamento, TipoNotificacao tipo);

    @Query("SELECT COUNT(n) FROM Notificacao n WHERE COALESCE(n.lida, FALSE) = FALSE")
    long contarNaoLidas();
}
