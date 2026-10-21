package com.example.pi3sem.backend.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.example.pi3sem.backend.model.AplicacaoVacina;

/**
 * Próxima dose de uma vacina para um pet, calculada a partir da última aplicação:
 * proximaDose = data da última aplicação + periodicidade da vacina (em dias).
 * situacao: VENCIDA (já passou), PROXIMA (dentro do prazo consultado) ou EM_DIA.
 */
public record ProximaDoseDTO(
        Integer idAplicacao,
        Integer idPet,
        String pet,
        Integer idTutor,
        String tutor,
        Integer idVacina,
        String vacina,
        Integer numeroDose,
        LocalDate ultimaAplicacao,
        LocalDate proximaDose,
        long diasRestantes,
        String situacao) {

    public static ProximaDoseDTO de(AplicacaoVacina aplicacao, LocalDate hoje, int diasAlerta) {
        LocalDate ultima = aplicacao.getDataAplicacao().toLocalDate();
        LocalDate proxima = ultima.plusDays(aplicacao.getVacina().getPeriodicidadeDias());
        long dias = ChronoUnit.DAYS.between(hoje, proxima);
        String situacao = dias < 0 ? "VENCIDA" : dias <= diasAlerta ? "PROXIMA" : "EM_DIA";
        return new ProximaDoseDTO(
                aplicacao.getIdAplicacaoVacina(),
                aplicacao.getPet().getIdPet(),
                aplicacao.getPet().getNome(),
                aplicacao.getPet().getTutor().getIdTutor(),
                aplicacao.getPet().getTutor().getNome(),
                aplicacao.getVacina().getIdVacina(),
                aplicacao.getVacina().getNome(),
                aplicacao.getNumeroDose(),
                ultima,
                proxima,
                dias,
                situacao);
    }
}
