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
import com.example.pi3sem.backend.model.Raca;
import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.service.EspecieService;
import com.example.pi3sem.backend.service.RacaService;
import com.example.pi3sem.backend.service.VacinaEspecieService;

@RestController
@RequestMapping("/especies")
public class EspecieController {

    private final EspecieService service;
    private final RacaService racaService;
    private final VacinaEspecieService vacinaEspecieService;

    public EspecieController(EspecieService service, RacaService racaService,
            VacinaEspecieService vacinaEspecieService) {
        this.service = service;
        this.racaService = racaService;
        this.vacinaEspecieService = vacinaEspecieService;
    }

    @GetMapping
    public List<Especie> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Especie buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Especie salvar(@RequestBody Especie especie) {
        return service.salvar(especie);
    }

    @PutMapping("/{id}")
    public Especie atualizar(@PathVariable Integer id, @RequestBody Especie especie) {
        return service.atualizar(id, especie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        service.excluir(id);
    }

    @GetMapping("/{id}/racas")
    public List<Raca> listarRacas(@PathVariable Integer id) {
        return racaService.listarPorEspecie(id);
    }

    @GetMapping("/{id}/vacinas")
    public List<Vacina> listarVacinas(@PathVariable Integer id) {
        return vacinaEspecieService.listarVacinasDaEspecie(id);
    }

}
