package com.example.pi3sem.backend.service.notificacao;

import com.example.pi3sem.backend.model.CanalNotificacao;
import com.example.pi3sem.backend.model.Notificacao;

/**
 * Forma de envio de uma notificação. Cada canal (WhatsApp, e-mail...) tem a sua
 * implementação; o NotificacaoService escolhe pelo campo "canal" da notificação.
 * Para adicionar um canal novo basta criar outra classe @Component que implemente esta interface.
 */
public interface NotificacaoSender {

    CanalNotificacao getCanal();

    // Envia a notificação e devolve uma informação para o cliente (ex.: link do WhatsApp) ou null.
    // Deve lançar IllegalArgumentException quando faltar algum dado (ex.: tutor sem telefone)
    String enviar(Notificacao notificacao);
}
