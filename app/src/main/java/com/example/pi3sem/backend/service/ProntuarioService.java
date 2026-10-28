package com.example.pi3sem.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.dto.ProntuarioDTO;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.Pet;
import com.example.pi3sem.backend.model.StatusAgendamento;
import com.example.pi3sem.backend.model.Telefone;

// Junta as informações de várias entidades para a tela de Prontuário (RF06)
@Service
public class ProntuarioService {

    private static final int DIAS_ALERTA = 30;

    private final PetService petService;
    private final TelefoneService telefoneService;
    private final AplicacaoVacinaService aplicacaoVacinaService;
    private final AgendamentoService agendamentoService;

    public ProntuarioService(PetService petService, TelefoneService telefoneService,
            AplicacaoVacinaService aplicacaoVacinaService, AgendamentoService agendamentoService) {
        this.petService = petService;
        this.telefoneService = telefoneService;
        this.aplicacaoVacinaService = aplicacaoVacinaService;
        this.agendamentoService = agendamentoService;
    }

    public ProntuarioDTO gerar(Integer idPet) {
        Pet pet = petService.buscarPorId(idPet);
        List<String> telefones = telefoneService.listarPorTutor(pet.getTutor().getIdTutor()).stream()
                .map(Telefone::getNumero)
                .toList();
        LocalDateTime agora = LocalDateTime.now();
        List<Agendamento> agendamentosFuturos = agendamentoService.listarPorPet(idPet).stream()
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO
                        || a.getStatus() == StatusAgendamento.CONFIRMADO)
                .filter(a -> !a.getDataHora().isBefore(agora))
                .toList();
        return new ProntuarioDTO(
                pet,
                telefones,
                aplicacaoVacinaService.listarPorPet(idPet),
                agendamentosFuturos,
                aplicacaoVacinaService.listarDosesDoPet(idPet, DIAS_ALERTA));
    }
}
