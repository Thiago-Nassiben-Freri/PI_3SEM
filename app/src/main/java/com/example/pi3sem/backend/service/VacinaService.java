package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.repository.VacinaEspecieRepository;
import com.example.pi3sem.backend.repository.VacinaRepository;

@Service 
public class VacinaService {

    private final VacinaRepository repository;
    private final VacinaEspecieRepository vacinaEspecieRepository;

    public VacinaService(VacinaRepository repository, VacinaEspecieRepository vacinaEspecieRepository) {
        this.repository = repository;
        this.vacinaEspecieRepository = vacinaEspecieRepository;
    }

    public List<Vacina> listar() {
        return repository.findAll();
    }

    public Vacina buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vacina", id));
    }

    public Vacina salvar(Vacina vacina) {
        validar(vacina);
        return repository.save(vacina);
    }

    public Vacina atualizar(Integer id, Vacina dados) {
        Vacina vacina = buscarPorId(id);
        validar(dados);
        vacina.setNome(dados.getNome());
        vacina.setTipo(dados.getTipo());
        vacina.setDosagem(dados.getDosagem());
        vacina.setPeriodicidadeDias(dados.getPeriodicidadeDias());
        vacina.setFabricante(dados.getFabricante());
        return repository.save(vacina);
    }

    // Vínculos com espécies são removidos junto com a vacina; se houver
    // agendamentos ou aplicações, o banco recusa e tudo é desfeito (409 pelo handler)
    @Transactional
    public void excluir(Integer id) {
        Vacina vacina = buscarPorId(id);
        vacinaEspecieRepository.deleteAll(vacinaEspecieRepository.findByVacina_IdVacina(id));
        repository.delete(vacina);
    }

    private void validar(Vacina vacina) {
        if (vacina == null) {
            throw new IllegalArgumentException("A vacina não pode ser nula");
        }
        if (vacina.getNome() == null || vacina.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da vacina é obrigatório");
        }
        if (vacina.getTipo() == null || vacina.getTipo().isBlank()) {
            throw new IllegalArgumentException("O tipo da vacina é obrigatório");
        } 
        if (vacina.getDosagem() == null || vacina.getDosagem().isBlank()) {
            throw new IllegalArgumentException("A dosagem da vacina é obrigatória");
        }
        if (vacina.getPeriodicidadeDias() == null) {
            throw new IllegalArgumentException("A periodicidade da vacina é obrigatória");
        }
        if (vacina.getPeriodicidadeDias() <= 0) {
            throw new IllegalArgumentException("A periodicidade da vacina deve ser maior que zero");
        }
    }

}
