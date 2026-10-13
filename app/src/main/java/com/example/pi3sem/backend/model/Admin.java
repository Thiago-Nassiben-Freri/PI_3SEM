package com.example.pi3sem.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "admin")
public class Admin {

    // Atributos
    @ManyToOne 
    @JoinColumn (name = "fk_usuario_id_usuario", nullable = false)
    private Usuario usuario;

    // Getters e Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
