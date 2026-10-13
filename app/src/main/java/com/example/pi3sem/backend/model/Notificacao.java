package com.example.pi3sem.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificacao")
public class Notificacao {

    // Atributos
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_notificacao")
    private Integer idNotificacao;

    @Column (name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column (name = "canal", nullable = false, length = 20)
    private String canal;

    @Column (name = "mensagem", nullable = false)
    private String mensagem;

    @Column (name = "data_envio", nullable = false)
    private LocalDate dataEnvio;

    @Column (name = "status", nullable = false, length = 20)
    private String status;

    @ManyToOne 
    @JoinColumn (name = "fk_agendamento_id_agendamento", nullable = false)
    private Agendamento agendamento;

    @ManyToOne 
    @JoinColumn (name = "fk_veterinario_id_usuario", nullable = false)
    private Veterinario veterinario;

    @ManyToOne 
    @JoinColumn (name = "fk_aplicacao_vacina_id_aplicacao_vacina", nullable = false)
    private AplicacaoVacina aplicacaoVacina;

    @ManyToOne 
    @JoinColumn (name = "fk_tutor_id_tutor", nullable = false)
    private Tutor tutor;

    // Getters e Setters
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
