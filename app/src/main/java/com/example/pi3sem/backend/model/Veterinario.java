package com.example.pi3sem.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "veterinario")
public class Veterinario {

    // Atributos
    @Column (name = "crmv", unique = true, nullable = false)
    private Integer crmv;

    @Column (name = "telefone", nullable = false, length = 15)
    private String telefone;

    @ManyToOne
    @JoinColumn (name = "fk_usuario_id_usuario", nullable = false)
    private Usuario usuario;

    // Getters e Setters
    public Integer getCrmv() {
        return crmv;
    }

    public void setCrmv(Integer crmv) {
        this.crmv = crmv;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
