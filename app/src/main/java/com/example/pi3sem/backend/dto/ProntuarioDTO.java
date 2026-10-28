package com.example.pi3sem.backend.dto;

import java.util.List;

import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.AplicacaoVacina;
import com.example.pi3sem.backend.model.Pet;

/**
 * Prontuário do pet (tela de Prontuário): dados do pet e do tutor, telefones do tutor,
 * histórico de aplicações (mais recente primeiro), agendamentos em aberto
 * e a situação da próxima dose de cada vacina já aplicada.
 */
public record ProntuarioDTO(
        Pet pet,
        List<String> telefonesTutor,
        List<AplicacaoVacina> historico,
        List<Agendamento> agendamentosFuturos,
        List<ProximaDoseDTO> proximasDoses) {
}
