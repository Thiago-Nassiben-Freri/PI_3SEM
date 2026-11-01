package com.example.pi3sem.desktop.model;

import java.util.List;

// Resumo da tela Início (GET /dashboard/resumo)
public class Dashboard {

    private long totalPets;
    private long totalTutores;
    private long totalVacinas;
    private long agendamentosHoje;
    private long dosesProximas;
    private long dosesVencidas;
    private long notificacoesNaoLidas;
    private long notificacoesPendentes;
    private List<ProximaDose> vacinasProximas;
    private List<Agendamento> compromissosDoDia;

    public long getTotalPets() {
        return totalPets;
    }

    public void setTotalPets(long totalPets) {
        this.totalPets = totalPets;
    }

    public long getTotalTutores() {
        return totalTutores;
    }

    public void setTotalTutores(long totalTutores) {
        this.totalTutores = totalTutores;
    }

    public long getTotalVacinas() {
        return totalVacinas;
    }

    public void setTotalVacinas(long totalVacinas) {
        this.totalVacinas = totalVacinas;
    }

    public long getAgendamentosHoje() {
        return agendamentosHoje;
    }

    public void setAgendamentosHoje(long agendamentosHoje) {
        this.agendamentosHoje = agendamentosHoje;
    }

    public long getDosesProximas() {
        return dosesProximas;
    }

    public void setDosesProximas(long dosesProximas) {
        this.dosesProximas = dosesProximas;
    }

    public long getDosesVencidas() {
        return dosesVencidas;
    }

    public void setDosesVencidas(long dosesVencidas) {
        this.dosesVencidas = dosesVencidas;
    }

    public long getNotificacoesNaoLidas() {
        return notificacoesNaoLidas;
    }

    public void setNotificacoesNaoLidas(long notificacoesNaoLidas) {
        this.notificacoesNaoLidas = notificacoesNaoLidas;
    }

    public long getNotificacoesPendentes() {
        return notificacoesPendentes;
    }

    public void setNotificacoesPendentes(long notificacoesPendentes) {
        this.notificacoesPendentes = notificacoesPendentes;
    }

    public List<ProximaDose> getVacinasProximas() {
        return vacinasProximas;
    }

    public void setVacinasProximas(List<ProximaDose> vacinasProximas) {
        this.vacinasProximas = vacinasProximas;
    }

    public List<Agendamento> getCompromissosDoDia() {
        return compromissosDoDia;
    }

    public void setCompromissosDoDia(List<Agendamento> compromissosDoDia) {
        this.compromissosDoDia = compromissosDoDia;
    }
}