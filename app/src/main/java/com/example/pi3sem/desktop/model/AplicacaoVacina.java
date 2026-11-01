package com.example.pi3sem.desktop.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Registro de uma vacina aplicada em um animal
public class AplicacaoVacina {

    private Integer idAplicacaoVacina;
    private LocalDateTime dataAplicacao;
    private Integer numeroDose;
    private String lote;
    private LocalDate validadeLote;
    private String observacao;
    private Pet pet;
    private Vacina vacina;
    private Veterinario veterinario;

    public Integer getIdAplicacaoVacina() {
        return idAplicacaoVacina;
    }

    public void setIdAplicacaoVacina(Integer idAplicacaoVacina) {
        this.idAplicacaoVacina = idAplicacaoVacina;
    }

    public LocalDateTime getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDateTime dataAplicacao) {
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

    public LocalDate getValidadeLote() {
        return validadeLote;
    }

    public void setValidadeLote(LocalDate validadeLote) {
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
}