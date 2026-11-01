package com.example.pi3sem.desktop.model;

import java.util.List;

// Prontuário de um animal (GET /pets/{id}/prontuario)
public class Prontuario {

    private Pet pet;
    private List<String> telefonesTutor;
    private List<AplicacaoVacina> historico;
    private List<Agendamento> agendamentosFuturos;
    private List<ProximaDose> proximasDoses;

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public List<String> getTelefonesTutor() {
        return telefonesTutor;
    }

    public void setTelefonesTutor(List<String> telefonesTutor) {
        this.telefonesTutor = telefonesTutor;
    }

    public List<AplicacaoVacina> getHistorico() {
        return historico;
    }

    public void setHistorico(List<AplicacaoVacina> historico) {
        this.historico = historico;
    }

    public List<Agendamento> getAgendamentosFuturos() {
        return agendamentosFuturos;
    }

    public void setAgendamentosFuturos(List<Agendamento> agendamentosFuturos) {
        this.agendamentosFuturos = agendamentosFuturos;
    }

    public List<ProximaDose> getProximasDoses() {
        return proximasDoses;
    }

    public void setProximasDoses(List<ProximaDose> proximasDoses) {
        this.proximasDoses = proximasDoses;
    }
}