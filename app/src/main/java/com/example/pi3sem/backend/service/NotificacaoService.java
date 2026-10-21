package com.example.pi3sem.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pi3sem.backend.dto.EnvioNotificacaoDTO;
import com.example.pi3sem.backend.dto.ProximaDoseDTO;
import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.AplicacaoVacina;
import com.example.pi3sem.backend.model.CanalNotificacao;
import com.example.pi3sem.backend.model.Notificacao;
import com.example.pi3sem.backend.model.StatusAgendamento;
import com.example.pi3sem.backend.model.StatusNotificacao;
import com.example.pi3sem.backend.model.TipoNotificacao;
import com.example.pi3sem.backend.model.Tutor;
import com.example.pi3sem.backend.model.Veterinario;
import com.example.pi3sem.backend.repository.NotificacaoRepository;
import com.example.pi3sem.backend.service.notificacao.NotificacaoSender;

@Service
public class NotificacaoService {

    // Usados quando o filtro de período não é informado
    private static final LocalDate INICIO_PADRAO = LocalDate.of(1900, 1, 1);
    private static final LocalDate FIM_PADRAO = LocalDate.of(9999, 12, 31);
    // Lembretes de agendamento são gerados para os atendimentos dos próximos 2 dias
    private static final int DIAS_LEMBRETE_AGENDAMENTO = 2;
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final NotificacaoRepository repository;
    private final TutorService tutorService;
    private final VeterinarioService veterinarioService;
    private final AgendamentoService agendamentoService;
    private final AplicacaoVacinaService aplicacaoVacinaService;
    private final List<NotificacaoSender> senders;

    public NotificacaoService(NotificacaoRepository repository, TutorService tutorService,
            VeterinarioService veterinarioService, AgendamentoService agendamentoService,
            AplicacaoVacinaService aplicacaoVacinaService, List<NotificacaoSender> senders) {
        this.repository = repository;
        this.tutorService = tutorService;
        this.veterinarioService = veterinarioService;
        this.agendamentoService = agendamentoService;
        this.aplicacaoVacinaService = aplicacaoVacinaService;
        this.senders = senders;
    }

    public List<Notificacao> listar() {
        return repository.findAll();
    }

    public List<Notificacao> filtrar(StatusNotificacao status, TipoNotificacao tipo, CanalNotificacao canal,
            Boolean lida, Integer tutorId, LocalDate inicio, LocalDate fim) {
        LocalDate de = inicio != null ? inicio : INICIO_PADRAO;
        LocalDate ate = fim != null ? fim : FIM_PADRAO;
        if (de.isAfter(ate)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final");
        }
        return repository.filtrar(de, ate, status, tipo, canal, lida, tutorId);
    }

    public Notificacao buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Notificação", id));
    }

    public long contarNaoLidas() {
        return repository.contarNaoLidas();
    }

    // Cria a notificação como PENDENTE, para revisão antes do envio (tela de envio via WhatsApp)
    public Notificacao salvar(Notificacao notificacao) {
        validar(notificacao);
        if (notificacao.getTutor() == null || notificacao.getTutor().getIdTutor() == null) {
            throw new IllegalArgumentException("O tutor da notificação é obrigatório");
        }
        if (notificacao.getVeterinario() == null || notificacao.getVeterinario().getId() == null) {
            throw new IllegalArgumentException("O veterinário da notificação é obrigatório");
        }
        Tutor tutor = tutorService.buscarPorId(notificacao.getTutor().getIdTutor());
        notificacao.setTutor(tutor);
        notificacao.setVeterinario(veterinarioService.buscarPorId(notificacao.getVeterinario().getId()));
        if (notificacao.getAgendamento() != null && notificacao.getAgendamento().getIdAgendamento() != null) {
            Agendamento agendamento = agendamentoService.buscarPorId(notificacao.getAgendamento().getIdAgendamento());
            validarMesmoTutor(tutor, agendamento.getPet().getTutor(), "O agendamento");
            notificacao.setAgendamento(agendamento);
        } else {
            notificacao.setAgendamento(null);
        }
        if (notificacao.getAplicacaoVacina() != null && notificacao.getAplicacaoVacina().getIdAplicacaoVacina() != null) {
            AplicacaoVacina aplicacao = aplicacaoVacinaService.buscarPorId(
                    notificacao.getAplicacaoVacina().getIdAplicacaoVacina());
            validarMesmoTutor(tutor, aplicacao.getPet().getTutor(), "A aplicação de vacina");
            notificacao.setAplicacaoVacina(aplicacao);
        } else {
            notificacao.setAplicacaoVacina(null);
        }
        notificacao.setIdNotificacao(null);
        notificacao.setStatus(StatusNotificacao.PENDENTE);
        notificacao.setLida(false);
        return repository.save(notificacao);
    }

    // Só notificações ainda não enviadas podem ser editadas
    public Notificacao atualizar(Integer id, Notificacao dados) {
        Notificacao notificacao = buscarPorId(id);
        if (notificacao.getStatus() == StatusNotificacao.ENVIADA) {
            throw new IllegalArgumentException("Uma notificação já enviada não pode ser alterada");
        }
        validar(dados);
        notificacao.setTipo(dados.getTipo());
        notificacao.setCanal(dados.getCanal());
        notificacao.setMensagem(dados.getMensagem());
        notificacao.setDataEnvio(dados.getDataEnvio());
        return repository.save(notificacao);
    }

    // Envia pelo canal da notificação. Se o envio falhar, a notificação fica com status FALHA
    // e pode ser enviada de novo
    public EnvioNotificacaoDTO enviar(Integer id) {
        Notificacao notificacao = buscarPorId(id);
        if (notificacao.getStatus() == StatusNotificacao.ENVIADA) {
            throw new IllegalArgumentException("Esta notificação já foi enviada");
        }
        NotificacaoSender sender = senders.stream()
                .filter(s -> s.getCanal() == notificacao.getCanal())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "O canal " + notificacao.getCanal() + " não está disponível"));
        String link;
        try {
            link = sender.enviar(notificacao);
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            notificacao.setStatus(StatusNotificacao.FALHA);
            repository.save(notificacao);
            throw new IllegalArgumentException("Falha ao enviar a notificação: " + ex.getMessage());
        }
        notificacao.setStatus(StatusNotificacao.ENVIADA);
        notificacao.setDataEnvio(LocalDate.now());
        return new EnvioNotificacaoDTO(repository.save(notificacao), link);
    }

    public Notificacao marcarComoLida(Integer id) {
        Notificacao notificacao = buscarPorId(id);
        notificacao.setLida(true);
        return repository.save(notificacao);
    }

    /**
     * Gera notificações PENDENTES (sem enviar) para:
     * - doses que vencem nos próximos "dias" (PROXIMA_DOSE) e doses atrasadas (DOSE_ATRASADA),
     *   exceto quando o pet já tem agendamento em aberto para a mesma vacina;
     * - agendamentos em aberto dos próximos 2 dias (LEMBRETE_AGENDAMENTO).
     * Não duplica: cada aplicação/agendamento recebe no máximo uma notificação de cada tipo.
     */
    @Transactional
    public List<Notificacao> gerarPendentes(Integer dias) {
        List<Notificacao> criadas = new ArrayList<>();
        List<ProximaDoseDTO> doses = new ArrayList<>(aplicacaoVacinaService.listarProximasDoses(dias));
        doses.addAll(aplicacaoVacinaService.listarVencidas());
        for (ProximaDoseDTO dose : doses) {
            TipoNotificacao tipo = dose.situacao().equals("VENCIDA")
                    ? TipoNotificacao.DOSE_ATRASADA : TipoNotificacao.PROXIMA_DOSE;
            if (repository.existsByAplicacaoVacina_IdAplicacaoVacinaAndTipo(dose.idAplicacao(), tipo)
                    || possuiAgendamentoEmAberto(dose.idPet(), dose.idVacina())) {
                continue;
            }
            AplicacaoVacina aplicacao = aplicacaoVacinaService.buscarPorId(dose.idAplicacao());
            criadas.add(criar(tipo, mensagemDose(dose, tipo), aplicacao.getPet().getTutor(),
                    aplicacao.getVeterinario(), null, aplicacao));
        }
        LocalDateTime agora = LocalDateTime.now();
        for (Agendamento agendamento : agendamentoService.filtrar(agora, agora.plusDays(DIAS_LEMBRETE_AGENDAMENTO), null, null)) {
            if (!emAberto(agendamento)
                    || repository.existsByAgendamento_IdAgendamentoAndTipo(
                            agendamento.getIdAgendamento(), TipoNotificacao.LEMBRETE_AGENDAMENTO)) {
                continue;
            }
            criadas.add(criar(TipoNotificacao.LEMBRETE_AGENDAMENTO, mensagemAgendamento(agendamento),
                    agendamento.getPet().getTutor(), agendamento.getVeterinario(), agendamento, null));
        }
        return criadas;
    }

    private Notificacao criar(TipoNotificacao tipo, String mensagem, Tutor tutor, Veterinario veterinario,
            Agendamento agendamento, AplicacaoVacina aplicacao) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipo(tipo);
        notificacao.setCanal(CanalNotificacao.WHATSAPP);
        notificacao.setMensagem(mensagem);
        notificacao.setDataEnvio(LocalDate.now());
        notificacao.setStatus(StatusNotificacao.PENDENTE);
        notificacao.setLida(false);
        notificacao.setTutor(tutor);
        notificacao.setVeterinario(veterinario);
        notificacao.setAgendamento(agendamento);
        notificacao.setAplicacaoVacina(aplicacao);
        return repository.save(notificacao);
    }

    private String mensagemDose(ProximaDoseDTO dose, TipoNotificacao tipo) {
        if (tipo == TipoNotificacao.DOSE_ATRASADA) {
            return "Olá, " + dose.tutor() + "! A vacina " + dose.vacina() + " do(a) " + dose.pet()
                    + " está atrasada desde " + dose.proximaDose().format(DATA)
                    + ". Entre em contato com a Vet Home para agendar a aplicação o quanto antes.";
        }
        return "Olá, " + dose.tutor() + "! A próxima dose da vacina " + dose.vacina() + " do(a) " + dose.pet()
                + " está prevista para " + dose.proximaDose().format(DATA)
                + ". Entre em contato com a Vet Home para agendar a aplicação.";
    }

    private String mensagemAgendamento(Agendamento agendamento) {
        return "Olá, " + agendamento.getPet().getTutor().getNome() + "! Lembramos que a vacinação do(a) "
                + agendamento.getPet().getNome() + " (" + agendamento.getVacina().getNome() + ") está agendada para "
                + agendamento.getDataHora().format(DATA) + " às " + agendamento.getDataHora().format(HORA) + ".";
    }

    private boolean possuiAgendamentoEmAberto(Integer idPet, Integer idVacina) {
        LocalDateTime agora = LocalDateTime.now();
        return agendamentoService.listarPorPet(idPet).stream()
                .anyMatch(a -> emAberto(a)
                        && a.getVacina().getIdVacina().equals(idVacina)
                        && !a.getDataHora().isBefore(agora));
    }

    private boolean emAberto(Agendamento agendamento) {
        return agendamento.getStatus() == StatusAgendamento.AGENDADO
                || agendamento.getStatus() == StatusAgendamento.CONFIRMADO;
    }

    private void validarMesmoTutor(Tutor tutor, Tutor tutorDoRegistro, String registro) {
        if (!tutor.getIdTutor().equals(tutorDoRegistro.getIdTutor())) {
            throw new IllegalArgumentException(registro + " informado(a) é de um pet de outro tutor");
        }
    }

    private void validar(Notificacao notificacao) {
        if (notificacao == null) {
            throw new IllegalArgumentException("A notificação não pode ser nula");
        }
        if (notificacao.getTipo() == null) {
            throw new IllegalArgumentException("O tipo da notificação é obrigatório");
        }
        if (notificacao.getCanal() == null) {
            notificacao.setCanal(CanalNotificacao.WHATSAPP);
        }
        if (notificacao.getMensagem() == null || notificacao.getMensagem().isBlank()) {
            throw new IllegalArgumentException("A mensagem da notificação é obrigatória");
        }
        if (notificacao.getDataEnvio() == null) {
            notificacao.setDataEnvio(LocalDate.now());
        }
        if (notificacao.getDataEnvio().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de envio da notificação não pode estar no passado");
        }
    }

}
