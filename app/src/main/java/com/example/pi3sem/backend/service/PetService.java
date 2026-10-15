package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.Pet;
import com.example.pi3sem.backend.repository.PetRepository;

@Service 
public class PetService {

    private final PetRepository repository;
    private final TutorService tutorService;
    private final RacaService racaService;

    public PetService(PetRepository repository, TutorService tutorService, RacaService racaService) {
        this.repository = repository;
        this.tutorService = tutorService;
        this.racaService = racaService;
    }

    public List<Pet> listar() {
        return repository.findAll();
    }

    public Pet salvar(Pet pet) {
        validar(pet);
        pet.setSexo(Character.toUpperCase(pet.getSexo()));
        pet.setTutor(tutorService.buscarPorId(pet.getTutor().getIdTutor()));
        pet.setRaca(racaService.buscarPorId(pet.getRaca().getIdRaca()));
        return repository.save(pet);
    }

    private void validar(Pet pet) {
        if (pet == null) {
            throw new IllegalArgumentException("O pet não pode ser nulo");
        }
        if (pet.getNome() == null || pet.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do pet é obrigatório");
        }
        if (pet.getSexo() == null || (Character.toUpperCase(pet.getSexo()) != 'M' && Character.toUpperCase(pet.getSexo()) != 'F')) {
            throw new IllegalArgumentException("O sexo do pet é obrigatório e deve ser 'M' ou 'F'");
        }
        if (pet.getDataNascimento() == null) {
            throw new IllegalArgumentException("A data de nascimento do pet é obrigatória");
        }
        if (pet.getPeso() == null || pet.getPeso() <= 0) {
            throw new IllegalArgumentException("O peso do pet é obrigatório e deve ser maior que zero");
        }
        if (pet.getTutor() == null || pet.getTutor().getIdTutor() == null) {
            throw new IllegalArgumentException("O tutor do pet é obrigatório");
        }
        if (pet.getRaca() == null || pet.getRaca().getIdRaca() == null) {
            throw new IllegalArgumentException("A raça do pet é obrigatória");
        }
    }

}   
