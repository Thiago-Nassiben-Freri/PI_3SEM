package com.example.pi3sem.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.Pet;
import com.example.pi3sem.backend.model.StatusAgendamento;
import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.model.Veterinario;
import com.example.pi3sem.backend.repository.AgendamentoRepository;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final PetService petService;
    private final VacinaService vacinaService;
    private final VeterinarioService veterinarioService;
    private final VacinaEspecieService vacinaEspecieService;

    public AgendamentoService(AgendamentoRepository repository, PetService petService,
            VacinaService vacinaService, VeterinarioService veterinarioService,
            VacinaEspecieService vacinaEspecieService) {
        this.repository = repository;
        this.petService = petService;
        this.vacinaService = vacinaService;
        this.veterinarioService = veterinarioService;
        this.vacinaEspecieService = vacinaEspecieService;
    }

    public List<Agendamento> listar() {
        return repository.findAll();
    }

    public Agendamento salvar(Agendamento agendamento) {
        if (agendamento == null) {
            throw new IllegalArgumentException("O agendamento não pode ser nulo");
        }
        if (agendamento.getDataHora() == null) {
            throw new IllegalArgumentException("A data e hora do agendamento são obrigatórias");
        }
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data e hora do agendamento não podem estar no passado");
        }
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(StatusAgendamento.AGENDADO);
        }
        if (agendamento.getStatus() == StatusAgendamento.REALIZADO
                || agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new IllegalArgumentException("Um agendamento novo deve ter status AGENDADO ou CONFIRMADO");
        }
        if (agendamento.getPet() == null || agendamento.getPet().getIdPet() == null) {
            throw new IllegalArgumentException("O pet do agendamento é obrigatório");
        }
        if (agendamento.getVacina() == null || agendamento.getVacina().getIdVacina() == null) {
            throw new IllegalArgumentException("A vacina do agendamento é obrigatória");
        }
        if (agendamento.getVeterinario() == null || agendamento.getVeterinario().getId() == null) {
            throw new IllegalArgumentException("O veterinário do agendamento é obrigatório");
        }
        Pet pet = petService.buscarPorId(agendamento.getPet().getIdPet());
        Vacina vacina = vacinaService.buscarPorId(agendamento.getVacina().getIdVacina());
        Veterinario veterinario = veterinarioService.buscarPorId(agendamento.getVeterinario().getId());
        validarVacinaParaEspecie(vacina, pet);
        if (!Boolean.TRUE.equals(veterinario.getUsuario().getAtivo())) {
            throw new IllegalArgumentException("O veterinário está inativo e não pode receber agendamentos");
        }
        agendamento.setIdAgendamento(null);
        agendamento.setPet(pet);
        agendamento.setVacina(vacina);
        agendamento.setVeterinario(veterinario);
        return repository.save(agendamento);
    }

    // A vacina precisa estar vinculada à espécie do pet (tabela vacina_especie)
    private void validarVacinaParaEspecie(Vacina vacina, Pet pet) {
        Integer idEspecie = pet.getRaca().getEspecie().getIdEspecie();
        if (!vacinaEspecieService.vacinaServeParaEspecie(vacina.getIdVacina(), idEspecie)) {
            throw new IllegalArgumentException("A vacina " + vacina.getNome()
                    + " não é indicada para a espécie " + pet.getRaca().getEspecie().getNome());
        }
    }

}
