package com.example.pi3sem.backend.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "aplicacao_vacina")
public class AplicacaoVacina {

    // Atributos
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_aplicacao_vacina")
    private Integer idAplicacaoVacina;

    @Column (name = "data_aplicacao", nullable = false)
    private String dataAplicacao;

    @Column (name = "numero_dose", nullable = true)
    private Integer numeroDose;

    @Column (name = "lote", nullable = true, length = 50)
    private String lote;

    @Column (name = "validade_lote", nullable = true)
    private Date validadeLote;

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

    @ManyToOne 
    @JoinColumn (name = "fk_agendamento_id_agendamento", nullable = true)
    private Agendamento agendamento;

    // Getters e Setters
    public Integer getIdAplicacaoVacina() {
        return idAplicacaoVacina;
    }

    public void setIdAplicacaoVacina(Integer idAplicacaoVacina) {
        this.idAplicacaoVacina = idAplicacaoVacina;
    }

    public String getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(String dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public Integer getNumeroDose() {
        return numeroDose;
    }

    public void setNumeroDose(Integer numeroDose) {
        this.numeroDose = numeroDose;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Date getValidadeLote() {
        return validadeLote;
    }

    public void setValidadeLote(Date validadeLote) {
        this.validadeLote = validadeLote;
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

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
}
