package com.example.pi3sem.backend.dto;

import java.time.LocalDate;

/**
 * Linha do relatório de vacinações. O mesmo formato serve para as três situações:
 * REALIZADA (data da aplicação), AGENDADA (data do agendamento) e PENDENTE (data prevista da dose).
 * veterinario e numeroDose podem ser nulos quando não se aplicam.
 */
public record RelatorioVacinacaoDTO(
        String situacao,
        LocalDate data,
        Integer idPet,
        String pet,
        String tutor,
        String vacina,
        String veterinario,
        Integer numeroDose) {
}
