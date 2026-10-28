package com.example.pi3sem.backend.exception;

// Login ou senha incorretos, ou usuário inativo (HTTP 401)
public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
