package com.example.pi3sem.backend.service.notificacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.pi3sem.backend.service.NotificacaoService;

/**
 * Gera automaticamente as notificações pendentes uma vez por dia (padrão: 08:00).
 * Fica DESLIGADO por padrão: como o banco é compartilhado, só a instância de produção
 * deve ligar, com petvax.notificacoes.geracao-automatica=true.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "petvax.notificacoes.geracao-automatica", havingValue = "true")
public class NotificacaoAgendador {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoAgendador.class);
    private static final int DIAS_ANTECEDENCIA = 30;

    private final NotificacaoService notificacaoService;

    public NotificacaoAgendador(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @Scheduled(cron = "${petvax.notificacoes.cron:0 0 8 * * *}")
    public void gerarNotificacoesDoDia() {
        int quantidade = notificacaoService.gerarPendentes(DIAS_ANTECEDENCIA).size();
        log.info("Geração automática de notificações: {} notificação(ões) criada(s)", quantidade);
    }
}
