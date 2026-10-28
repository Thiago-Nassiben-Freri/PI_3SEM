package com.example.pi3sem.backend.dto;

import java.util.List;

import com.example.pi3sem.backend.model.Agendamento;

/**
 * Dados da tela de Visão Geral: indicadores, vacinas próximas do vencimento
 * (próximos 30 dias) e compromissos do dia.
 */
public record DashboardDTO(
        long totalPets,
        long totalTutores,
        long totalVacinas,
        long agendamentosHoje,
        long dosesProximas,
        long dosesVencidas,
        long notificacoesNaoLidas,
        long notificacoesPendentes,
        List<ProximaDoseDTO> vacinasProximas,
        List<Agendamento> compromissosDoDia) {
}
