package com.example.pi3sem.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.Pet;
import com.example.pi3sem.backend.model.StatusAgendamento;
import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.model.Veterinario;
import com.example.pi3sem.backend.repository.AgendamentoRepository;

@Service
public class AgendamentoService {

    // Usados quando o filtro de período não é informado
    private static final LocalDateTime INICIO_PADRAO = LocalDateTime.of(1900, 1, 1, 0, 0);
    private static final LocalDateTime FIM_PADRAO = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

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

    public List<Agendamento> filtrar(LocalDateTime inicio, LocalDateTime fim, Integer veterinarioId,
            StatusAgendamento status) {
        LocalDateTime de = inicio != null ? inicio : INICIO_PADRAO;
        LocalDateTime ate = fim != null ? fim : FIM_PADRAO;
        if (de.isAfter(ate)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final");
        }
        return repository.filtrar(de, ate, veterinarioId, status);
    }

    public List<Agendamento> listarDeHoje() {
        LocalDate hoje = LocalDate.now();
        return filtrar(hoje.atStartOfDay(), hoje.atTime(LocalTime.MAX), null, null);
    }

    public List<Agendamento> listarPorPet(Integer idPet) {
        petService.buscarPorId(idPet);
        return repository.findByPet_IdPetOrderByDataHoraAsc(idPet);
    }

    public Agendamento buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento", id));
    }

    public Agendamento salvar(Agendamento agendamento) {
        validar(agendamento);
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(StatusAgendamento.AGENDADO);
        }
        if (agendamento.getStatus() == StatusAgendamento.REALIZADO
                || agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new IllegalArgumentException("Um agendamento novo deve ter status AGENDADO ou CONFIRMADO");
        }
        agendamento.setIdAgendamento(null);
        resolverRelacionamentos(agendamento, agendamento);
        return repository.save(agendamento);
    }

    // Remarcar: altera data, pet, vacina, veterinário e observação; o status é mantido
    public Agendamento atualizar(Integer id, Agendamento dados) {
        Agendamento agendamento = buscarPorId(id);
        validarEmAberto(agendamento, "remarcado");
        validar(dados);
        agendamento.setDataHora(dados.getDataHora());
        agendamento.setObservacao(dados.getObservacao());
        resolverRelacionamentos(dados, agendamento);
        return repository.save(agendamento);
    }

    // REALIZADO só é definido ao registrar a aplicação (POST /agendamentos/{id}/aplicacao),
    // para que todo agendamento realizado tenha a aplicação no histórico
    public Agendamento alterarStatus(Integer id, StatusAgendamento novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("O status é obrigatório");
        }
        Agendamento agendamento = buscarPorId(id);
        validarEmAberto(agendamento, "alterado");
        if (novoStatus == StatusAgendamento.REALIZADO) {
            throw new IllegalArgumentException(
                    "Para marcar como REALIZADO, registre a aplicação em POST /agendamentos/{id}/aplicacao");
        }
        agendamento.setStatus(novoStatus);
        return repository.save(agendamento);
    }

    // Usado pelo registro da aplicação da vacina
    public Agendamento marcarComoRealizado(Agendamento agendamento) {
        validarEmAberto(agendamento, "realizado");
        agendamento.setStatus(StatusAgendamento.REALIZADO);
        return repository.save(agendamento);
    }

    // Usado ao excluir a aplicação vinculada: o agendamento volta a ficar em aberto
    public Agendamento reabrir(Agendamento agendamento) {
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        return repository.save(agendamento);
    }

    public void excluir(Integer id) {
        Agendamento agendamento = buscarPorId(id);
        if (agendamento.getStatus() == StatusAgendamento.REALIZADO) {
            throw new IllegalArgumentException("Um agendamento realizado não pode ser excluído");
        }
        repository.delete(agendamento);
    }

    private void validar(Agendamento agendamento) {
        if (agendamento == null) {
            throw new IllegalArgumentException("O agendamento não pode ser nulo");
        }
        if (agendamento.getDataHora() == null) {
            throw new IllegalArgumentException("A data e hora do agendamento são obrigatórias");
        }
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data e hora do agendamento não podem estar no passado");
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
    }

    private void validarEmAberto(Agendamento agendamento, String acao) {
        if (agendamento.getStatus() == StatusAgendamento.REALIZADO
                || agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new IllegalArgumentException(
                    "Um agendamento " + agendamento.getStatus() + " não pode ser " + acao);
        }
    }

    // Busca pet, vacina e veterinário de "origem" pelo id e aplica em "destino"
    private void resolverRelacionamentos(Agendamento origem, Agendamento destino) {
        Pet pet = petService.buscarPorId(origem.getPet().getIdPet());
        Vacina vacina = vacinaService.buscarPorId(origem.getVacina().getIdVacina());
        Veterinario veterinario = veterinarioService.buscarPorId(origem.getVeterinario().getId());
        validarVacinaParaEspecie(vacina, pet);
        if (!Boolean.TRUE.equals(veterinario.getUsuario().getAtivo())) {
            throw new IllegalArgumentException("O veterinário está inativo e não pode receber agendamentos");
        }
        destino.setPet(pet);
        destino.setVacina(vacina);
        destino.setVeterinario(veterinario);
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
