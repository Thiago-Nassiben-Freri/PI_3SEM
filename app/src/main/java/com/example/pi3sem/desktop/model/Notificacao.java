package com.example.pi3sem.desktop.model;

import java.time.LocalDate;

// Notificação (lembrete) para um tutor
// tipo: LEMBRETE_AGENDAMENTO, PROXIMA_DOSE, DOSE_ATRASADA ou OUTRO
// canal: WHATSAPP, EMAIL ou SMS | status: PENDENTE, ENVIADA ou FALHA
public class Notificacao {

    private Integer idNotificacao;
    private String tipo;
    private String canal;
    private String mensagem;
    private LocalDate dataEnvio;
    private String status;
    private Boolean lida;
    private Agendamento agendamento;
    private Veterinario veterinario;
    private AplicacaoVacina aplicacaoVacina;
    private Tutor tutor;

    public Integer getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(Integer idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public AplicacaoVacina getAplicacaoVacina() {
        return aplicacaoVacina;
    }

    public void setAplicacaoVacina(AplicacaoVacina aplicacaoVacina) {
        this.aplicacaoVacina = aplicacaoVacina;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
}