package com.example.pi3sem.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.AplicacaoVacina;
import com.example.pi3sem.backend.model.StatusAgendamento;
import com.example.pi3sem.backend.service.AgendamentoService;
import com.example.pi3sem.backend.service.AplicacaoVacinaService;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;
    private final AplicacaoVacinaService aplicacaoVacinaService;

    public AgendamentoController(AgendamentoService service, AplicacaoVacinaService aplicacaoVacinaService) {
        this.service = service;
        this.aplicacaoVacinaService = aplicacaoVacinaService;
    }

    // Datas no formato ISO: 2026-11-01T08:00:00
    @GetMapping
    public List<Agendamento> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(required = false) Integer veterinarioId,
            @RequestParam(required = false) StatusAgendamento status) {
        return service.filtrar(inicio, fim, veterinarioId, status);
    }

    @GetMapping("/hoje")
    public List<Agendamento> listarDeHoje() {
        return service.listarDeHoje();
    }

    @GetMapping("/{id}")
    public Agendamento buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agendamento salvar(@RequestBody Agendamento agendamento) {
        return service.salvar(agendamento);
    }

    @PutMapping("/{id}")
    public Agendamento atualizar(@PathVariable Integer id, @RequestBody Agendamento agendamento) {
        return service.atualizar(id, agendamento);
    }

    // Corpo: {"status": "CONFIRMADO"} ou {"status": "CANCELADO"}
    @PatchMapping("/{id}/status")
    public Agendamento alterarStatus(@PathVariable Integer id, @RequestBody Agendamento agendamento) {
        return service.alterarStatus(id, agendamento.getStatus());
    }

    // Registra a vacina aplicada no atendimento e marca o agendamento como REALIZADO.
    // Corpo: {"numeroDose": 1, "lote": "...", "validadeLote": "2027-01-01"}; pet, vacina
    // e veterinário vêm do agendamento
    @PostMapping("/{id}/aplicacao")
    @ResponseStatus(HttpStatus.CREATED)
    public AplicacaoVacina registrarAplicacao(@PathVariable Integer id, @RequestBody AplicacaoVacina aplicacao) {
        return aplicacaoVacinaService.registrarDeAgendamento(id, aplicacao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        service.excluir(id);
    }
}
