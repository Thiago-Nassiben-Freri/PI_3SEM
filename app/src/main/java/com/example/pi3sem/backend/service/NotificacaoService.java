package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.Notificacao;
import com.example.pi3sem.backend.repository.NotificacaoRepository;

@Service 
public class NotificacaoService {

    private final NotificacaoRepository repository;

    public NotificacaoService(NotificacaoRepository repository) {
        this.repository = repository;
    }

    public List<Notificacao> listar() {
        return repository.findAll();
    }

    public Notificacao salvar(Notificacao notificacao) {
        if (notificacao == null) {
            throw new IllegalArgumentException("A notificação não pode ser nula");
        }
        if (notificacao.getTipo() == null || notificacao.getTipo().isBlank()) {
            throw new IllegalArgumentException("O tipo da notificação é obrigatório");
        }
        if (notificacao.getCanal() == null || notificacao.getCanal().isBlank()) {
            throw new IllegalArgumentException("O canal da notificação é obrigatório");
        }
        if (notificacao.getMensagem() == null || notificacao.getMensagem().isBlank()) {
            throw new IllegalArgumentException("A mensagem da notificação é obrigatória");
        }
        if (notificacao.getDataEnvio() == null || notificacao.getDataEnvio().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("A data e hora da notificação é obrigatória");
        }
        if (notificacao.getStatus() == null || notificacao.getStatus().isBlank()) {
            throw new IllegalArgumentException("O status da notificação é obrigatório");
        }
        if (notificacao.getAgendamento() == null || notificacao.getAgendamento().getIdAgendamento() == null) {
            throw new IllegalArgumentException("O agendamento da notificação é obrigatório");
        }
        if (notificacao.getVeterinario() == null || notificacao.getVeterinario().getUsuario() == null || notificacao.getVeterinario().getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("O veterinário da notificação é obrigatório");
        }
        if (notificacao.getAplicacaoVacina() == null || notificacao.getAplicacaoVacina().getIdAplicacaoVacina() == null) {
            throw new IllegalArgumentException("A aplicação da vacina da notificação é obrigatória");
        }
        if (notificacao.getTutor() == null || notificacao.getTutor().getIdTutor() == null) {
            throw new IllegalArgumentException("O tutor da notificação é obrigatório");
        }
        return repository.save(notificacao);
    }

}

