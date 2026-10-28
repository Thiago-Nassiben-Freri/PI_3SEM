package com.example.pi3sem.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.model.Especie;
import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.model.VacinaEspecie;
import com.example.pi3sem.backend.service.VacinaEspecieService;
import com.example.pi3sem.backend.service.VacinaService;

@RestController
@RequestMapping("/vacinas")
public class VacinaController {

    private final VacinaService service;
    private final VacinaEspecieService vacinaEspecieService;

    public VacinaController(VacinaService service, VacinaEspecieService vacinaEspecieService) {
        this.service = service;
        this.vacinaEspecieService = vacinaEspecieService;
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

    @GetMapping("/{id}")
    public Vacina buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Vacina atualizar(@PathVariable Integer id, @RequestBody Vacina vacina) {
        return service.atualizar(id, vacina);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        service.excluir(id);
    }

    @GetMapping("/{id}/especies")
    public List<Especie> listarEspecies(@PathVariable Integer id) {
        return vacinaEspecieService.listarEspeciesDaVacina(id);
    }

    @PostMapping("/{id}/especies/{especieId}")
    @ResponseStatus(HttpStatus.CREATED)
    public VacinaEspecie vincularEspecie(@PathVariable Integer id, @PathVariable Integer especieId) {
        return vacinaEspecieService.vincular(id, especieId);
    }

    @DeleteMapping("/{id}/especies/{especieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desvincularEspecie(@PathVariable Integer id, @PathVariable Integer especieId) {
        vacinaEspecieService.desvincular(id, especieId);
    }
}
