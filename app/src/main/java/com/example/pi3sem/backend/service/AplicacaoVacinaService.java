package com.example.pi3sem.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pi3sem.backend.dto.ProximaDoseDTO;
import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.AplicacaoVacina;
import com.example.pi3sem.backend.model.Pet;
import com.example.pi3sem.backend.model.StatusAgendamento;
import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.repository.AplicacaoVacinaRepository;

@Service
public class AplicacaoVacinaService {

    // Usados quando o filtro de período não é informado
    private static final LocalDate INICIO_PADRAO = LocalDate.of(1900, 1, 1);
    private static final LocalDate FIM_PADRAO = LocalDate.of(9999, 12, 31);

    private final AplicacaoVacinaRepository repository;
    private final PetService petService;
    private final VacinaService vacinaService;
    private final VeterinarioService veterinarioService;
    private final AgendamentoService agendamentoService;
    private final VacinaEspecieService vacinaEspecieService;

    public AplicacaoVacinaService(AplicacaoVacinaRepository repository, PetService petService,
            VacinaService vacinaService, VeterinarioService veterinarioService,
            AgendamentoService agendamentoService, VacinaEspecieService vacinaEspecieService) {
        this.repository = repository;
        this.petService = petService;
        this.vacinaService = vacinaService;
        this.veterinarioService = veterinarioService;
        this.agendamentoService = agendamentoService;
        this.vacinaEspecieService = vacinaEspecieService;
    }

    public List<AplicacaoVacina> listar() {
        return repository.findAll();
    }

    public List<AplicacaoVacina> filtrar(Integer petId, Integer vacinaId, LocalDate inicio, LocalDate fim) {
        LocalDate de = inicio != null ? inicio : INICIO_PADRAO;
        LocalDate ate = fim != null ? fim : FIM_PADRAO;
        if (de.isAfter(ate)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final");
        }
        return repository.filtrar(de.atStartOfDay(), ate.atTime(LocalTime.MAX), petId, vacinaId);
    }

    // Histórico do pet, mais recente primeiro
    public List<AplicacaoVacina> listarPorPet(Integer idPet) {
        petService.buscarPorId(idPet);
        return repository.findByPet_IdPetOrderByDataAplicacaoDesc(idPet);
    }

    public AplicacaoVacina buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aplicação de vacina", id));
    }

    // Se vier com agendamento, ele precisa ser do mesmo pet e vacina e passa a REALIZADO
    @Transactional
    public AplicacaoVacina salvar(AplicacaoVacina aplicacao) {
        validar(aplicacao);
        Pet pet = petService.buscarPorId(aplicacao.getPet().getIdPet());
        Vacina vacina = vacinaService.buscarPorId(aplicacao.getVacina().getIdVacina());
        validarVacinaParaEspecie(vacina, pet);
        aplicacao.setPet(pet);
        aplicacao.setVacina(vacina);
        aplicacao.setVeterinario(veterinarioService.buscarPorId(aplicacao.getVeterinario().getId()));
        if (aplicacao.getAgendamento() != null && aplicacao.getAgendamento().getIdAgendamento() != null) {
            Agendamento agendamento = agendamentoService.buscarPorId(aplicacao.getAgendamento().getIdAgendamento());
            if (!agendamento.getPet().getIdPet().equals(pet.getIdPet())
                    || !agendamento.getVacina().getIdVacina().equals(vacina.getIdVacina())) {
                throw new IllegalArgumentException("O agendamento informado é de outro pet ou de outra vacina");
            }
            aplicacao.setAgendamento(agendamentoService.marcarComoRealizado(agendamento));
        } else {
            aplicacao.setAgendamento(null);
        }
        aplicacao.setIdAplicacaoVacina(null);
        return repository.save(aplicacao);
    }

    // POST /agendamentos/{id}/aplicacao: pet e vacina vêm do agendamento; o veterinário
    // também, a menos que outro seja informado; a data padrão é o momento do registro
    @Transactional
    public AplicacaoVacina registrarDeAgendamento(Integer idAgendamento, AplicacaoVacina dados) {
        Agendamento agendamento = agendamentoService.buscarPorId(idAgendamento);
        AplicacaoVacina aplicacao = dados != null ? dados : new AplicacaoVacina();
        aplicacao.setPet(agendamento.getPet());
        aplicacao.setVacina(agendamento.getVacina());
        if (aplicacao.getVeterinario() == null || aplicacao.getVeterinario().getId() == null) {
            aplicacao.setVeterinario(agendamento.getVeterinario());
        }
        if (aplicacao.getDataAplicacao() == null) {
            aplicacao.setDataAplicacao(LocalDateTime.now());
        }
        aplicacao.setAgendamento(agendamento);
        return salvar(aplicacao);
    }

    // Aplicação vinculada a agendamento não pode trocar de pet nem de vacina
    public AplicacaoVacina atualizar(Integer id, AplicacaoVacina dados) {
        AplicacaoVacina aplicacao = buscarPorId(id);
        validar(dados);
        Pet pet = petService.buscarPorId(dados.getPet().getIdPet());
        Vacina vacina = vacinaService.buscarPorId(dados.getVacina().getIdVacina());
        if (aplicacao.getAgendamento() != null
                && (!pet.getIdPet().equals(aplicacao.getPet().getIdPet())
                        || !vacina.getIdVacina().equals(aplicacao.getVacina().getIdVacina()))) {
            throw new IllegalArgumentException(
                    "Esta aplicação está vinculada a um agendamento: o pet e a vacina não podem ser alterados");
        }
        validarVacinaParaEspecie(vacina, pet);
        aplicacao.setPet(pet);
        aplicacao.setVacina(vacina);
        aplicacao.setVeterinario(veterinarioService.buscarPorId(dados.getVeterinario().getId()));
        aplicacao.setDataAplicacao(dados.getDataAplicacao());
        aplicacao.setNumeroDose(dados.getNumeroDose());
        aplicacao.setLote(dados.getLote());
        aplicacao.setValidadeLote(dados.getValidadeLote());
        aplicacao.setObservacao(dados.getObservacao());
        return repository.save(aplicacao);
    }

    // Se a aplicação veio de um agendamento, ele volta a ficar em aberto
    @Transactional
    public void excluir(Integer id) {
        AplicacaoVacina aplicacao = buscarPorId(id);
        Agendamento agendamento = aplicacao.getAgendamento();
        repository.delete(aplicacao);
        if (agendamento != null && agendamento.getStatus() == StatusAgendamento.REALIZADO) {
            agendamentoService.reabrir(agendamento);
        }
    }

    // Doses que vencem entre hoje e hoje + dias
    public List<ProximaDoseDTO> listarProximasDoses(Integer dias) {
        int prazo = dias != null ? dias : 30;
        if (prazo < 0) {
            throw new IllegalArgumentException("O número de dias não pode ser negativo");
        }
        return calcularDoses(repository.buscarUltimasAplicacoes(), prazo).stream()
                .filter(dose -> dose.situacao().equals("PROXIMA"))
                .toList();
    }

    // Doses cuja data prevista já passou
    public List<ProximaDoseDTO> listarVencidas() {
        return calcularDoses(repository.buscarUltimasAplicacoes(), 30).stream()
                .filter(dose -> dose.situacao().equals("VENCIDA"))
                .toList();
    }

    // Situação de todas as vacinas já aplicadas no pet (usado no prontuário)
    public List<ProximaDoseDTO> listarDosesDoPet(Integer idPet, int diasAlerta) {
        return calcularDoses(repository.buscarUltimasAplicacoesDoPet(idPet), diasAlerta);
    }

    private List<ProximaDoseDTO> calcularDoses(List<AplicacaoVacina> ultimasAplicacoes, int diasAlerta) {
        LocalDate hoje = LocalDate.now();
        return ultimasAplicacoes.stream()
                .map(aplicacao -> ProximaDoseDTO.de(aplicacao, hoje, diasAlerta))
                .sorted(Comparator.comparing(ProximaDoseDTO::proximaDose))
                .toList();
    }

    private void validar(AplicacaoVacina aplicacao) {
        if (aplicacao == null) {
            throw new IllegalArgumentException("A aplicação da vacina não pode ser nula");
        }
        if (aplicacao.getDataAplicacao() == null) {
            throw new IllegalArgumentException("A data de aplicação da vacina é obrigatória");
        }
        if (aplicacao.getDataAplicacao().isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("A data de aplicação da vacina não pode estar no futuro");
        }
        if (aplicacao.getNumeroDose() == null || aplicacao.getNumeroDose() <= 0) {
            throw new IllegalArgumentException("O número da dose da aplicação da vacina é obrigatório");
        }
        if (aplicacao.getLote() == null || aplicacao.getLote().isBlank()) {
            throw new IllegalArgumentException("O lote da aplicação da vacina é obrigatório");
        }
        if (aplicacao.getLote().length() > 50) {
            throw new IllegalArgumentException("O lote deve ter no máximo 50 caracteres");
        }
        if (aplicacao.getValidadeLote() == null) {
            throw new IllegalArgumentException("A validade do lote da aplicação da vacina é obrigatória");
        }
        if (aplicacao.getValidadeLote().isBefore(aplicacao.getDataAplicacao().toLocalDate())) {
            throw new IllegalArgumentException("O lote estava vencido na data da aplicação");
        }
        if (aplicacao.getPet() == null || aplicacao.getPet().getIdPet() == null) {
            throw new IllegalArgumentException("O pet da aplicação da vacina é obrigatório");
        }
        if (aplicacao.getVacina() == null || aplicacao.getVacina().getIdVacina() == null) {
            throw new IllegalArgumentException("A vacina da aplicação da vacina é obrigatória");
        }
        if (aplicacao.getVeterinario() == null || aplicacao.getVeterinario().getId() == null) {
            throw new IllegalArgumentException("O veterinário da aplicação da vacina é obrigatório");
        }
    }

    private void validarVacinaParaEspecie(Vacina vacina, Pet pet) {
        Integer idEspecie = pet.getRaca().getEspecie().getIdEspecie();
        if (!vacinaEspecieService.vacinaServeParaEspecie(vacina.getIdVacina(), idEspecie)) {
            throw new IllegalArgumentException("A vacina " + vacina.getNome()
                    + " não é indicada para a espécie " + pet.getRaca().getEspecie().getNome());
        }
    }

}
