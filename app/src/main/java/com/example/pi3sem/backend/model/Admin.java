package com.example.pi3sem.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "admin")
public class Admin {

    // Atributos
    @Id 
    private Integer id;

    @OneToOne
    @MapsId  
    @JoinColumn (name = "fk_usuario_id_usuario", nullable = false)
    private Usuario usuario;

    // Getters e Setters
    public Integer getId() {
        return id;
    }   

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
