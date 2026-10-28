package com.example.pi3sem.backend.dto;

import com.example.pi3sem.backend.model.Notificacao;

/**
 * Resposta de POST /notificacoes/{id}/enviar.
 * link: no canal WHATSAPP é o link "wa.me" que o desktop deve abrir; nos demais canais é null.
 */
public record EnvioNotificacaoDTO(Notificacao notificacao, String link) {
}
