package com.example.pi3sem.desktop.model;

// Vacina do catálogo
public class Vacina {

    private Integer idVacina;
    private String nome;
    private String tipo;
    private String dosagem;
    private Integer periodicidadeDias;
    private String fabricante;

    public Integer getIdVacina() {
        return idVacina;
    }

    public void setIdVacina(Integer idVacina) {
        this.idVacina = idVacina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public Integer getPeriodicidadeDias() {
        return periodicidadeDias;
    }

    public void setPeriodicidadeDias(Integer periodicidadeDias) {
        this.periodicidadeDias = periodicidadeDias;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    // Texto que aparece quando o objeto é mostrado num ComboBox
    @Override
    public String toString() {
        return nome;
    }
}