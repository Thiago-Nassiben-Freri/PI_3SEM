package com.example.pi3sem.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.repository.AgendamentoRepository;

@Service 
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    public List<Agendamento> listar() {
        return repository.findAll();
    }

    public Agendamento salvar(Agendamento agendamento) {
        if (agendamento == null) {
            throw new IllegalArgumentException("O agendamento não pode ser nulo");
        }
        if (agendamento.getDataHora() == null || agendamento.getDataHora().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("A data e hora do agendamento são obrigatórias");
        }
        if (agendamento.getStatus() == null || agendamento.getStatus().isBlank()) {
            throw new IllegalArgumentException("O status do agendamento é obrigatório");
        }
        if (agendamento.getPet() == null || agendamento.getPet().getIdPet() == null) {
            throw new IllegalArgumentException("O pet do agendamento é obrigatório");
        }
        if (agendamento.getVacina() == null || agendamento.getVacina().getIdVacina() == null) {
            throw new IllegalArgumentException("A vacina do agendamento é obrigatória");
        }
        if (agendamento.getVeterinario() == null || agendamento.getVeterinario().getUsuario() == null || agendamento.getVeterinario().getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("O veterinário do agendamento é obrigatório");
        }
        return repository.save(agendamento);
    }

}
