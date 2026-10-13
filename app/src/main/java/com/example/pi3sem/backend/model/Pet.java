package com.example.pi3sem.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pet")
public class Pet {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet")
    private Integer idPet;

    @Column (name = "nome", nullable = false, length = 50)
    private String nome;

    @Column (name = "sexo", nullable = false, length = 1)
    private Character sexo;

    @Column (name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column (name = "peso", nullable = false)
    private Double peso;

    @Column (name = "observacoes", nullable = true)
    private String observacoes;

    @ManyToOne
    @JoinColumn (name = "fk_tutor_id_tutor", nullable = false)
    private Tutor tutor;

    @ManyToOne 
    @JoinColumn (name = "fk_raca_id_raca", nullable = false)
    private Raca raca;

    // Getters e Setters
    public Integer getIdPet() {
        return idPet;

    }
    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;   
    }

    public Character getSexo() {
        return sexo;
    }
    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Double getPeso() {
        return peso;
    }
    public void setPeso(Double peso) {  
        this.peso = peso;
    }

    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Tutor getTutor() {
        return tutor;
    }
    public void setTutor(Tutor tutor) {
        this.tutor = tutor; 
    }

    public Raca getRaca() {
        return raca;
    }
    
    public void setRaca(Raca raca) {
        this.raca = raca;
    }
}