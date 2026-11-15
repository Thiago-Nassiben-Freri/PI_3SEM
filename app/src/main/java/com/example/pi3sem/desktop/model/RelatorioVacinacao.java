package com.example.pi3sem.desktop.model;

import java.time.LocalDate;

// Linha do relatório de vacinações (GET /relatorios/vacinacoes)
// situacao: REALIZADA, AGENDADA ou PENDENTE
public class RelatorioVacinacao {

    private String situacao;
    private LocalDate data;
    private Integer idPet;
    private String pet;
    private String tutor;
    private String vacina;
    private String veterinario;
    private Integer numeroDose;

    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getIdPet() { return idPet; }
    public void setIdPet(Integer idPet) { this.idPet = idPet; }

    public String getPet() { return pet; }
    public void setPet(String pet) { this.pet = pet; }

    public String getTutor() { return tutor; }
    public void setTutor(String tutor) { this.tutor = tutor; }

    public String getVacina() { return vacina; }
    public void setVacina(String vacina) { this.vacina = vacina; }

    public String getVeterinario() { return veterinario; }
    public void setVeterinario(String veterinario) { this.veterinario = veterinario; }

    public Integer getNumeroDose() { return numeroDose; }
    public void setNumeroDose(Integer numeroDose) { this.numeroDose = numeroDose; }
}