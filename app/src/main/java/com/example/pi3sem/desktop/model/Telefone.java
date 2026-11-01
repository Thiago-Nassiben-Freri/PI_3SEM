package com.example.pi3sem.desktop.model;

// Telefone de um tutor
public class Telefone {

    private Integer idTelefone;
    private String numero;

    public Integer getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(Integer idTelefone) {
        this.idTelefone = idTelefone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    // Texto que aparece quando o objeto é mostrado num ComboBox
    @Override
    public String toString() {
        return numero;
    }
}