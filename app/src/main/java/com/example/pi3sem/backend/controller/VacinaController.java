package com.example.pi3sem.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.service.VacinaService;

@RestController
@RequestMapping("/vacinas")
public class VacinaController {

    private final VacinaService service;

    public VacinaController(VacinaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Vacina> listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vacina salvar(@RequestBody Vacina vacina) {
        return service.salvar(vacina);
    }
}
