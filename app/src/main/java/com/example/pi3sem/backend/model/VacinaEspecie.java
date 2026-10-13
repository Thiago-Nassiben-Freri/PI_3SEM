package com.example.pi3sem.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacina_especie")
public class VacinaEspecie {

    // Atributos
    @ManyToOne
    @JoinColumn(name = "fk_vacina_id_especie", nullable = false)
    private Vacina vacina;

    @ManyToOne
    @JoinColumn(name = "fk_especie_id_especie", nullable = false)
    private Especie especie;

    // Getters e Setters
    public Vacina getVacina() {
        return vacina;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }
}
