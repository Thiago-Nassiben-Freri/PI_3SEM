package com.example.pi3sem.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "raca")
public class Raca {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_raca")
    private Integer idRaca;

    @Column (name = "nome", nullable = false, length = 50)
    private String nome;

    @ManyToOne
    @JoinColumn (name = "fk_especie_id_especie", nullable = false)
    private Especie especie;

    // Getters e Setters
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
}
