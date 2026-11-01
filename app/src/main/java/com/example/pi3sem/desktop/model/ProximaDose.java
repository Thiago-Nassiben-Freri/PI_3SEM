package com.example.pi3sem.desktop.model;

import java.time.LocalDate;

// Próxima dose de uma vacina (GET /aplicacoes/proximas-doses e /vencidas)
// situacao: VENCIDA, PROXIMA ou EM_DIA
public class ProximaDose {

    private Integer idAplicacao;
    private Integer idPet;
    private String pet;
    private Integer idTutor;
    private String tutor;
    private Integer idVacina;
    private String vacina;
    private Integer numeroDose;
    private LocalDate ultimaAplicacao;
    private LocalDate proximaDose;
    private long diasRestantes;
    private String situacao;

    public Integer getIdAplicacao() {
        return idAplicacao;
    }

    public void setIdAplicacao(Integer idAplicacao) {
        this.idAplicacao = idAplicacao;
    }

    public Integer getIdPet() {
        return idPet;
    }

    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public Integer getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(Integer idTutor) {
        this.idTutor = idTutor;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public Integer getIdVacina() {
        return idVacina;
    }

    public void setIdVacina(Integer idVacina) {
        this.idVacina = idVacina;
    }

    public String getVacina() {
        return vacina;
    }

    public void setVacina(String vacina) {
        this.vacina = vacina;
    }

    public Integer getNumeroDose() {
        return numeroDose;
    }

    public void setNumeroDose(Integer numeroDose) {
        this.numeroDose = numeroDose;
    }

    public LocalDate getUltimaAplicacao() {
        return ultimaAplicacao;
    }

    public void setUltimaAplicacao(LocalDate ultimaAplicacao) {
        this.ultimaAplicacao = ultimaAplicacao;
    }

    public LocalDate getProximaDose() {
        return proximaDose;
    }

    public void setProximaDose(LocalDate proximaDose) {
        this.proximaDose = proximaDose;
    }

    public long getDiasRestantes() {
        return diasRestantes;
    }

    public void setDiasRestantes(long diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}