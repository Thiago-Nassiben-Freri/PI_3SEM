package com.example.pi3sem.backend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.dto.RelatorioVacinacaoDTO;
import com.example.pi3sem.backend.model.StatusAgendamento;

// Relatórios básicos: vacinações realizadas, agendadas e pendentes (macrofunção do escopo)
@Service
public class RelatorioService {

    private final AplicacaoVacinaService aplicacaoVacinaService;
    private final AgendamentoService agendamentoService;

    public RelatorioService(AplicacaoVacinaService aplicacaoVacinaService, AgendamentoService agendamentoService) {
        this.aplicacaoVacinaService = aplicacaoVacinaService;
        this.agendamentoService = agendamentoService;
    }

    /**
     * situacao:
     * - realizadas: aplicações feitas no período
     * - agendadas: agendamentos em aberto (AGENDADO/CONFIRMADO) no período
     * - pendentes: doses com data prevista no período; sem "fim", considera até hoje + 30 dias
     *   (inclui as atrasadas)
     */
    public List<RelatorioVacinacaoDTO> vacinacoes(String situacao, LocalDate inicio, LocalDate fim) {
        if (situacao == null || situacao.isBlank()) {
            throw new IllegalArgumentException("Informe a situação: realizadas, agendadas ou pendentes");
        }
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final");
        }
        return switch (situacao.toLowerCase()) {
            case "realizadas" -> realizadas(inicio, fim);
            case "agendadas" -> agendadas(inicio, fim);
            case "pendentes" -> pendentes(inicio, fim);
            default -> throw new IllegalArgumentException(
                    "Situação inválida: use realizadas, agendadas ou pendentes");
        };
    }

    private List<RelatorioVacinacaoDTO> realizadas(LocalDate inicio, LocalDate fim) {
        return aplicacaoVacinaService.filtrar(null, null, inicio, fim).stream()
                .map(a -> new RelatorioVacinacaoDTO(
                        "REALIZADA",
                        a.getDataAplicacao().toLocalDate(),
                        a.getPet().getIdPet(),
                        a.getPet().getNome(),
                        a.getPet().getTutor().getNome(),
                        a.getVacina().getNome(),
                        a.getVeterinario().getUsuario().getNome(),
                        a.getNumeroDose()))
                .toList();
    }

    private List<RelatorioVacinacaoDTO> agendadas(LocalDate inicio, LocalDate fim) {
        return agendamentoService.filtrar(
                        inicio != null ? inicio.atStartOfDay() : null,
                        fim != null ? fim.atTime(LocalTime.MAX) : null,
                        null, null).stream()
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO
                        || a.getStatus() == StatusAgendamento.CONFIRMADO)
                .map(a -> new RelatorioVacinacaoDTO(
                        "AGENDADA",
                        a.getDataHora().toLocalDate(),
                        a.getPet().getIdPet(),
                        a.getPet().getNome(),
                        a.getPet().getTutor().getNome(),
                        a.getVacina().getNome(),
                        a.getVeterinario().getUsuario().getNome(),
                        null))
                .toList();
    }

    private List<RelatorioVacinacaoDTO> pendentes(LocalDate inicio, LocalDate fim) {
        LocalDate ate = fim != null ? fim : LocalDate.now().plusDays(30);
        return aplicacaoVacinaService.listarTodasDoses().stream()
                .filter(d -> !d.proximaDose().isAfter(ate))
                .filter(d -> inicio == null || !d.proximaDose().isBefore(inicio))
                .sorted(Comparator.comparing(d -> d.proximaDose()))
                .map(d -> new RelatorioVacinacaoDTO(
                        "PENDENTE",
                        d.proximaDose(),
                        d.idPet(),
                        d.pet(),
                        d.tutor(),
                        d.vacina(),
                        null,
                        d.numeroDose() + 1))
                .toList();
    }
}
