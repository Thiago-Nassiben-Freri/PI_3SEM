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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.model.Telefone;
import com.example.pi3sem.backend.model.Tutor;
import com.example.pi3sem.backend.service.TelefoneService;
import com.example.pi3sem.backend.service.TutorService;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    private final TutorService service;
    private final TelefoneService telefoneService;

    public TutorController(TutorService service, TelefoneService telefoneService) {
        this.service = service;
        this.telefoneService = telefoneService;
    }

    @GetMapping
    public List<Tutor> listar(@RequestParam(required = false) String busca) {
        return service.listar(busca);
    }

    @GetMapping("/{id}")
    public Tutor buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tutor salvar(@RequestBody Tutor tutor) {
        return service.salvar(tutor);
    }

    @PutMapping("/{id}")
    public Tutor atualizar(@PathVariable Integer id, @RequestBody Tutor tutor) {
        return service.atualizar(id, tutor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        service.excluir(id);
    }

    @GetMapping("/{id}/telefones")
    public List<Telefone> listarTelefones(@PathVariable Integer id) {
        return telefoneService.listarPorTutor(id);
    }

    @PostMapping("/{id}/telefones")
    @ResponseStatus(HttpStatus.CREATED)
    public Telefone adicionarTelefone(@PathVariable Integer id, @RequestBody Telefone telefone) {
        return telefoneService.adicionar(id, telefone);
    }
}
