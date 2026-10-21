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
import com.example.pi3sem.backend.model.Agendamento;
import com.example.pi3sem.backend.model.Pet;
import com.example.pi3sem.backend.service.AgendamentoService;
import com.example.pi3sem.backend.service.PetService;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService service;
    private final AgendamentoService agendamentoService;

    public PetController(PetService service, AgendamentoService agendamentoService) {
        this.service = service;
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public List<Pet> listar(@RequestParam(required = false) String nome,
                            @RequestParam(required = false) Integer especieId,
                            @RequestParam(required = false) Integer tutorId) {
        return service.filtrar(nome, especieId, tutorId);
    }

    @GetMapping("/{id}")
    public Pet buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet salvar(@RequestBody Pet pet) {
        return service.salvar(pet);
    }

    @PutMapping("/{id}")
    public Pet atualizar(@PathVariable Integer id, @RequestBody Pet pet) {
        return service.atualizar(id, pet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        service.excluir(id);
    }

    @GetMapping("/{id}/agendamentos")
    public List<Agendamento> listarAgendamentos(@PathVariable Integer id) {
        return agendamentoService.listarPorPet(id);
    }
}
