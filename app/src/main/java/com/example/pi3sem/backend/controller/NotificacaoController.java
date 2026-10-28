package com.example.pi3sem.backend.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
import com.example.pi3sem.backend.dto.EnvioNotificacaoDTO;
import com.example.pi3sem.backend.model.CanalNotificacao;
import com.example.pi3sem.backend.model.Notificacao;
import com.example.pi3sem.backend.model.StatusNotificacao;
import com.example.pi3sem.backend.model.TipoNotificacao;
import com.example.pi3sem.backend.service.NotificacaoService;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    // Datas no formato ISO: 2026-11-01
    @GetMapping
    public List<Notificacao> listar(
            @RequestParam(required = false) StatusNotificacao status,
            @RequestParam(required = false) TipoNotificacao tipo,
            @RequestParam(required = false) CanalNotificacao canal,
            @RequestParam(required = false) Boolean lida,
            @RequestParam(required = false) Integer tutorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.filtrar(status, tipo, canal, lida, tutorId, inicio, fim);
    }

    @GetMapping("/{id}")
    public Notificacao buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notificacao salvar(@RequestBody Notificacao notificacao) {
        return service.salvar(notificacao);
    }

    @PutMapping("/{id}")
    public Notificacao atualizar(@PathVariable Integer id, @RequestBody Notificacao notificacao) {
        return service.atualizar(id, notificacao);
    }

    // No canal WHATSAPP a resposta traz o link wa.me que o desktop deve abrir
    @PostMapping("/{id}/enviar")
    public EnvioNotificacaoDTO enviar(@PathVariable Integer id) {
        return service.enviar(id);
    }

    @PatchMapping("/{id}/lida")
    public Notificacao marcarComoLida(@PathVariable Integer id) {
        return service.marcarComoLida(id);
    }

    // Gera lembretes de doses (próximos "dias" e atrasadas) e de agendamentos dos próximos 2 dias
    @PostMapping("/gerar-pendentes")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Notificacao> gerarPendentes(@RequestParam(required = false) Integer dias) {
        return service.gerarPendentes(dias);
    }
}
