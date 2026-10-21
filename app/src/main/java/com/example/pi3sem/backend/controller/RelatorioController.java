package com.example.pi3sem.backend.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.dto.RelatorioVacinacaoDTO;
import com.example.pi3sem.backend.service.RelatorioService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    // Ex.: /relatorios/vacinacoes?situacao=realizadas&inicio=2026-01-01&fim=2026-01-31
    @GetMapping("/vacinacoes")
    public List<RelatorioVacinacaoDTO> vacinacoes(
            @RequestParam(required = false) String situacao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.vacinacoes(situacao, inicio, fim);
    }
}
