package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.VacinaEspecie;
import com.example.pi3sem.backend.repository.VacinaEspecieRepository;

@Service 
public class VacinaEspecieService {

    private final VacinaEspecieRepository repository;

    public VacinaEspecieService(VacinaEspecieRepository repository) {
        this.repository = repository;
    }

    public List<VacinaEspecie> listar() {
        return  repository.findAll();
    }

    public VacinaEspecie salvar(VacinaEspecie vacinaEspecie) {
        if (vacinaEspecie == null) {
            throw new IllegalArgumentException("A vacinaEspecie é obrigatória");
        }
        if (vacinaEspecie.getVacina() == null || vacinaEspecie.getVacina().getIdVacina() == null) {
            throw new IllegalArgumentException("A Vacina é obrigatória");
        }
        if (vacinaEspecie.getEspecie() == null || vacinaEspecie.getEspecie().getIdEspecie() == null) {
            throw new IllegalArgumentException("A Espécie é obrigatória");
        }

        return repository.save(vacinaEspecie);
    }

}
