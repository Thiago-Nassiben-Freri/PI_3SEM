package com.example.pi3sem.desktop.model;

// Resposta de POST /notificacoes/{id}/enviar: a notificação e o link do WhatsApp
public class EnvioNotificacao {

    private Notificacao notificacao;
    private String link;

    public Notificacao getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Notificacao notificacao) {
        this.notificacao = notificacao;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}