package com.example.pi3sem.backend.service.notificacao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.pi3sem.backend.model.CanalNotificacao;
import com.example.pi3sem.backend.model.Notificacao;

/**
 * Envio por e-mail (RF08 - Could Have). Só é ativado quando o servidor SMTP está
 * configurado (spring.mail.host no application.properties); sem isso, notificações
 * com canal EMAIL retornam erro informando que o canal não está disponível.
 */
@Component
@ConditionalOnProperty(name = "spring.mail.host")
public class EmailNotificacaoSender implements NotificacaoSender {

    private final JavaMailSender mailSender;
    private final String remetente;

    public EmailNotificacaoSender(JavaMailSender mailSender,
            @Value("${petvax.mail.remetente:${spring.mail.username:}}") String remetente) {
        this.mailSender = mailSender;
        this.remetente = remetente;
    }

    @Override
    public CanalNotificacao getCanal() {
        return CanalNotificacao.EMAIL;
    }

    @Override
    public String enviar(Notificacao notificacao) {
        String email = notificacao.getTutor().getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O tutor não possui email cadastrado");
        }
        SimpleMailMessage mensagem = new SimpleMailMessage();
        if (!remetente.isBlank()) {
            mensagem.setFrom(remetente);
        }
        mensagem.setTo(email);
        mensagem.setSubject("PetVax - Lembrete de vacinação");
        mensagem.setText(notificacao.getMensagem());
        mailSender.send(mensagem);
        return null;
    }
}
