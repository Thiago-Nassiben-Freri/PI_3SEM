package com.example.pi3sem.backend.exception;

import java.time.LocalDateTime;

public record ErroResposta(int status, String mensagem, LocalDateTime timestamp) {

    public ErroResposta(int status, String mensagem) {
        this(status, mensagem, LocalDateTime.now());
    }
}
