package com.example.pi3sem.backend.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.dto.ProximaDoseDTO;
import com.example.pi3sem.backend.model.AplicacaoVacina;
import com.example.pi3sem.backend.service.AplicacaoVacinaService;

@RestController
@RequestMapping("/aplicacoes")
public class AplicacaoVacinaController {

    private final AplicacaoVacinaService service;

    public AplicacaoVacinaController(AplicacaoVacinaService service) {
        this.service = service;
    }

    // Datas no formato ISO: 2026-11-01
    @GetMapping
    public List<AplicacaoVacina> listar(
            @RequestParam(required = false) Integer petId,
            @RequestParam(required = false) Integer vacinaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.filtrar(petId, vacinaId, inicio, fim);
    }

    @GetMapping("/proximas-doses")
    public List<ProximaDoseDTO> listarProximasDoses(@RequestParam(required = false) Integer dias) {
        return service.listarProximasDoses(dias);
    }

    @GetMapping("/vencidas")
    public List<ProximaDoseDTO> listarVencidas() {
        return service.listarVencidas();
    }

    @GetMapping("/{id}")
    public AplicacaoVacina buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AplicacaoVacina salvar(@RequestBody AplicacaoVacina aplicacao) {
        return service.salvar(aplicacao);
    }

    @PutMapping("/{id}")
    public AplicacaoVacina atualizar(@PathVariable Integer id, @RequestBody AplicacaoVacina aplicacao) {
        return service.atualizar(id, aplicacao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        service.excluir(id);
    }
}
