package com.example.pi3sem.desktop.model;

// Tutor do animal (quem recebe as notificações)
public class Tutor {

    private Integer idTutor;
    private String cpf;
    private String nome;
    private String email;
    private String cep;
    private String numero;

    public Integer getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(Integer idTutor) {
        this.idTutor = idTutor;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    // Texto que aparece quando o objeto é mostrado num ComboBox
    @Override
    public String toString() {
        return nome;
    }
}