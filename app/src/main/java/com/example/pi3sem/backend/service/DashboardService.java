package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.dto.DashboardDTO;
import com.example.pi3sem.backend.dto.ProximaDoseDTO;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.repository.PetRepository;
import com.example.pi3sem.backend.repository.TutorRepository;
import com.example.pi3sem.backend.repository.VacinaRepository;

// Monta o resumo da tela de Visão Geral
@Service
public class DashboardService {

    private static final int DIAS_ALERTA = 30;

    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;
    private final VacinaRepository vacinaRepository;
    private final AgendamentoService agendamentoService;
    private final AplicacaoVacinaService aplicacaoVacinaService;
    private final NotificacaoService notificacaoService;

    public DashboardService(PetRepository petRepository, TutorRepository tutorRepository,
            VacinaRepository vacinaRepository, AgendamentoService agendamentoService,
            AplicacaoVacinaService aplicacaoVacinaService, NotificacaoService notificacaoService) {
        this.petRepository = petRepository;
        this.tutorRepository = tutorRepository;
        this.vacinaRepository = vacinaRepository;
        this.agendamentoService = agendamentoService;
        this.aplicacaoVacinaService = aplicacaoVacinaService;
        this.notificacaoService = notificacaoService;
    }

    public DashboardDTO gerarResumo() {
        List<ProximaDoseDTO> proximas = aplicacaoVacinaService.listarProximasDoses(DIAS_ALERTA);
        List<ProximaDoseDTO> vencidas = aplicacaoVacinaService.listarVencidas();
        List<Agendamento> hoje = agendamentoService.listarDeHoje();
        return new DashboardDTO(
                petRepository.count(),
                tutorRepository.count(),
                vacinaRepository.count(),
                hoje.size(),
                proximas.size(),
                vencidas.size(),
                notificacaoService.contarNaoLidas(),
                notificacaoService.contarPendentes(),
                proximas,
                hoje);
    }
}
