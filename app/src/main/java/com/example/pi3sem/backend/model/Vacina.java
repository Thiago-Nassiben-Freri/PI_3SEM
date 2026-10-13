package com.example.pi3sem.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacina")
public class Vacina {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacina")
    private Integer idVacina;

    @Column (name = "nome", nullable = false, length = 100)
    private String nome;

    @Column (name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column (name = "dosagem", nullable = false, length = 30)
    private String dosagem;

    @Column (name = "periodicidade_dias", nullable = false)
    private Integer periodicidadeDias;

    @Column (name = "fabricante", nullable = true, length = 100)
    private String fabricante;

    // Getters e Setters
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
}
