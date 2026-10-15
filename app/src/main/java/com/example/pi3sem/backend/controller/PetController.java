package com.example.pi3sem.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.pi3sem.backend.model.Pet;
import com.example.pi3sem.backend.service.PetService;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService service;

    public PetController(PetService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pet> listar() {
        return service.listar();
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
}
