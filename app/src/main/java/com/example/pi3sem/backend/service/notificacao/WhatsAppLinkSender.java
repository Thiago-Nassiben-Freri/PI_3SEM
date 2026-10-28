package com.example.pi3sem.backend.service.notificacao;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.pi3sem.backend.model.CanalNotificacao;
import com.example.pi3sem.backend.model.Notificacao;
import com.example.pi3sem.backend.model.Telefone;
import com.example.pi3sem.backend.repository.TelefoneRepository;

/**
 * Envio por WhatsApp via link "wa.me" (gratuito, sem API paga - RES03).
 * O backend monta o link com o telefone do tutor e a mensagem; o desktop abre o link
 * no navegador e o veterinário confirma o envio no WhatsApp Web/aplicativo.
 * Para envio automático no futuro, troque esta classe por uma integração com a
 * WhatsApp Cloud API mantendo a interface NotificacaoSender.
 */
@Component
public class WhatsAppLinkSender implements NotificacaoSender {

    private final TelefoneRepository telefoneRepository;

    public WhatsAppLinkSender(TelefoneRepository telefoneRepository) {
        this.telefoneRepository = telefoneRepository;
    }

    @Override
    public CanalNotificacao getCanal() {
        return CanalNotificacao.WHATSAPP;
    }

    @Override
    public String enviar(Notificacao notificacao) {
        List<Telefone> telefones = telefoneRepository.findByTutor_IdTutor(notificacao.getTutor().getIdTutor());
        if (telefones.isEmpty()) {
            throw new IllegalArgumentException("O tutor não possui telefone cadastrado para envio por WhatsApp");
        }
        String numero = formatarNumero(telefones.get(0).getNumero());
        String texto = URLEncoder.encode(notificacao.getMensagem(), StandardCharsets.UTF_8).replace("+", "%20");
        return "https://wa.me/" + numero + "?text=" + texto;
    }

    // Mantém só os dígitos e adiciona o código do Brasil (55) quando o número vier só com DDD
    private String formatarNumero(String numero) {
        String digitos = numero.replaceAll("\\D", "");
        if (digitos.length() == 10 || digitos.length() == 11) {
            return "55" + digitos;
        }
        return digitos;
    }
}
