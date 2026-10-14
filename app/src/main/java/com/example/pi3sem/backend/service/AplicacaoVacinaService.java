package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.AplicacaoVacina;
import com.example.pi3sem.backend.repository.AplicacaoVacinaRepository;

@Service 
public class AplicacaoVacinaService {

    private final AplicacaoVacinaRepository repository;

    public AplicacaoVacinaService(AplicacaoVacinaRepository repository) {
        this.repository = repository;
    }

    public List<AplicacaoVacina> listar() {
        return repository.findAll();
    }

    public AplicacaoVacina salvar(AplicacaoVacina aplicacaoVacina) {
        if (aplicacaoVacina == null) {
            throw new IllegalArgumentException("A aplicação da vacina não pode ser nula");
        }
        if (aplicacaoVacina.getDataAplicacao() == null) {
            throw new IllegalArgumentException("A data de aplicação da vacina é obrigatória");
        }
        if (aplicacaoVacina.getNumeroDose() == null || aplicacaoVacina.getNumeroDose() <= 0) {
            throw new IllegalArgumentException("O número da dose da aplicação da vacina é obrigatório");
        }
        if (aplicacaoVacina.getLote() == null || aplicacaoVacina.getLote().isBlank()) {
            throw new IllegalArgumentException("O lote da aplicação da vacina é obrigatório");
        }
        if (aplicacaoVacina.getValidadeLote() == null || aplicacaoVacina.getValidadeLote().before(new java.sql.Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("A validade do lote da aplicação da vacina é obrigatória");
        }
        if (aplicacaoVacina.getPet() == null || aplicacaoVacina.getPet().getIdPet() == null) {
            throw new IllegalArgumentException("O pet da aplicação da vacina é obrigatório");
        }
        if (aplicacaoVacina.getVacina() == null || aplicacaoVacina.getVacina().getIdVacina() == null) {
            throw new IllegalArgumentException("A vacina da aplicação da vacina é obrigatória");
        }
        if (aplicacaoVacina.getVeterinario() == null || aplicacaoVacina.getVeterinario().getUsuario() == null || aplicacaoVacina.getVeterinario().getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("O veterinário da aplicação da vacina é obrigatório");
        }
        if (aplicacaoVacina.getAgendamento() != null && aplicacaoVacina.getAgendamento().getIdAgendamento() == null) {
            throw new IllegalArgumentException("O agendamento da aplicação da vacina é inválido");
        }
        return repository.save(aplicacaoVacina);
    }

}
