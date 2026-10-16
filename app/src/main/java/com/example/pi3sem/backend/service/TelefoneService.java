package com.example.pi3sem.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.exception.RecursoNaoEncontradoException;
import com.example.pi3sem.backend.model.Telefone;
import com.example.pi3sem.backend.model.Tutor;
import com.example.pi3sem.backend.repository.TelefoneRepository;

@Service 
public class TelefoneService {

    private final TelefoneRepository repository;
    private final TutorService tutorService;

    public TelefoneService(TelefoneRepository repository, TutorService tutorService) {
        this.repository = repository;
        this.tutorService = tutorService;
    }

    public List<Telefone> listar() {
        return repository.findAll();
    }

    public List<Telefone> listarPorTutor(Integer idTutor) {
        tutorService.buscarPorId(idTutor);
        return repository.findByTutor_IdTutor(idTutor);
    }

    public Telefone salvar(Telefone telefone) {
        if (telefone == null) {
            throw new IllegalArgumentException("O telefone não pode ser nulo");
        }
        if (telefone.getNumero() == null || telefone.getNumero().isBlank()) {
            throw new IllegalArgumentException("O número do telefone é obrigatório");
        }
        if (telefone.getNumero().length() > 15) {
            throw new IllegalArgumentException("O número do telefone deve ter no máximo 15 caracteres");
        }
        if (telefone.getTutor() == null || telefone.getTutor().getIdTutor() == null) {
            throw new IllegalArgumentException("O tutor do telefone é obrigatório");
        }
        telefone.setTutor(tutorService.buscarPorId(telefone.getTutor().getIdTutor()));
        return repository.save(telefone);
    }

    // Cadastro pela rota /tutores/{id}/telefones: o tutor vem da URL, não do corpo
    public Telefone adicionar(Integer idTutor, Telefone telefone) {
        if (telefone == null) {
            throw new IllegalArgumentException("O telefone não pode ser nulo");
        }
        Tutor tutor = new Tutor();
        tutor.setIdTutor(idTutor);
        telefone.setIdTelefone(null);
        telefone.setTutor(tutor);
        return salvar(telefone);
    }

    // Só remove se o telefone pertencer ao tutor informado na URL
    public void excluir(Integer idTutor, Integer idTelefone) {
        Telefone telefone = repository.findById(idTelefone)
                .filter(t -> t.getTutor().getIdTutor().equals(idTutor))
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Telefone com id " + idTelefone + " não encontrado para o tutor " + idTutor));
        repository.delete(telefone);
    }

}
