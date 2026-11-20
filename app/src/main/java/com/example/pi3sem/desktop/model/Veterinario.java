package com.example.pi3sem.desktop.model;

// Veterinário (o id é o mesmo id do usuário)
public class Veterinario {

    private Integer id;
    private Integer crmv;
    private String telefone;

    @Override
    public String toString() {
        if (crmv != null) {
            return "CRMV " + crmv;
        }
        if (id != null) {
            return "Veterinário #" + id;
        }
        return "Veterinário";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}