package com.example.pi3sem.desktop.model;

// Espécie do animal (cão, gato...)
public class Especie {

    private Integer idEspecie;
    private String nome;

    public Integer getIdEspecie() {
        return idEspecie;
    }

    public void setIdEspecie(Integer idEspecie) {
        this.idEspecie = idEspecie;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Texto que aparece quando o objeto é mostrado num ComboBox
    @Override
    public String toString() {
        return nome;
    }
}