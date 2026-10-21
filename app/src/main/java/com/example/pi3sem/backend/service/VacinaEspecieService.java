package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
import com.example.pi3sem.backend.model.Especie;
import com.example.pi3sem.backend.model.VacinaEspecie;
import com.example.pi3sem.backend.repository.VacinaEspecieRepository;

@Service 
public class VacinaEspecieService {

    private final VacinaEspecieRepository repository;
    private final VacinaService vacinaService;
    private final EspecieService especieService;

    public VacinaEspecieService(VacinaEspecieRepository repository, VacinaService vacinaService,
            EspecieService especieService) {
        this.repository = repository;
        this.vacinaService = vacinaService;
        this.especieService = especieService;
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
        return vincular(vacinaEspecie.getVacina().getIdVacina(), vacinaEspecie.getEspecie().getIdEspecie());
    }

    public List<Especie> listarEspeciesDaVacina(Integer idVacina) {
        vacinaService.buscarPorId(idVacina);
        return repository.findByVacina_IdVacina(idVacina).stream()
                .map(VacinaEspecie::getEspecie)
                .toList();
    }

    // Indica que a vacina pode ser aplicada na espécie
    public VacinaEspecie vincular(Integer idVacina, Integer idEspecie) {
        VacinaEspecie vinculo = new VacinaEspecie();
        vinculo.setVacina(vacinaService.buscarPorId(idVacina));
        vinculo.setEspecie(especieService.buscarPorId(idEspecie));
        if (repository.existsByVacina_IdVacinaAndEspecie_IdEspecie(idVacina, idEspecie)) {
            throw new IllegalArgumentException("Esta vacina já está vinculada a esta espécie");
        }
        return repository.save(vinculo);
    }

    public void desvincular(Integer idVacina, Integer idEspecie) {
        VacinaEspecie vinculo = repository.findByVacina_IdVacinaAndEspecie_IdEspecie(idVacina, idEspecie)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vínculo entre a vacina " + idVacina + " e a espécie " + idEspecie + " não encontrado"));
        repository.delete(vinculo);
    }

}
