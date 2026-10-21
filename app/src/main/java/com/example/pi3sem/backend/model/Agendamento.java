package com.example.pi3sem.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "agendamento")
public class Agendamento {

    // Atributos 
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_agendamento")
    private Integer idAgendamento;

    @Column (name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated (EnumType.STRING)
    @Column (name = "status", columnDefinition = "VARCHAR(20) DEFAULT 'AGENDADO'", nullable = false)
    private StatusAgendamento status;

    @Column (name = "observacao", nullable = true)
    private String observacao;

    @ManyToOne 
    @JoinColumn (name = "fk_pet_id_pet", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn (name = "fk_vacina_id_vacina", nullable = false)
    private Vacina vacina;

    @ManyToOne
    @JoinColumn (name = "fk_veterinario_fk_usuario", nullable = false)
    private Veterinario veterinario;


    // Getters e Setters
    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Vacina getVacina() {
        return vacina;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
}


