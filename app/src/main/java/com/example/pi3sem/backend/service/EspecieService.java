package com.example.pi3sem.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
import com.example.pi3sem.backend.model.Especie;
import com.example.pi3sem.backend.repository.EspecieRepository;

@Service
public class EspecieService {

    private final EspecieRepository repository;

    public EspecieService(EspecieRepository repository) {
        this.repository = repository;
    }

    public List<Especie> listar() {
        return repository.findAll();
    }

    public Especie buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Espécie", id));
    }

    public Especie salvar(Especie especie) {
        validar(especie);
        return repository.save(especie);
    }

    public Especie atualizar(Integer id, Especie dados) {
        Especie especie = buscarPorId(id);
        validar(dados);
        especie.setNome(dados.getNome());
        return repository.save(especie);
    }

    public void excluir(Integer id) {
        Especie especie = buscarPorId(id);
        repository.delete(especie);
    }

    private void validar(Especie especie) {
        if (especie.getNome() == null || especie.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da espécie é obrigatório");
        }
    }
}

