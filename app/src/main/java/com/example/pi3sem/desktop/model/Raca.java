package com.example.pi3sem.desktop.model;

// Raça do animal, ligada a uma espécie
public class Raca {

    private Integer idRaca;
    private String nome;
    private Especie especie;

    public Integer getIdRaca() {
        return idRaca;
    }

    public void setIdRaca(Integer idRaca) {
        this.idRaca = idRaca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    // Texto que aparece quando o objeto é mostrado num ComboBox
    @Override
    public String toString() {
        return nome;
    }
}