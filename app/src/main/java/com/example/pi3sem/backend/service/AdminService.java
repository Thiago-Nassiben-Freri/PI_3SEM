package com.example.pi3sem.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.pi3sem.backend.model.Admin;
import com.example.pi3sem.backend.repository.AdminRepository;

@Service
public class AdminService {

    private final AdminRepository repository;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    public List<Admin> listar() {
        return repository.findAll();
    }

    public Admin salvar(Admin admin) {
        if (admin ==  null) {
            throw new IllegalArgumentException("O admin não pode ser nulo");
        }
        if (admin.getUsuario() == null) {
            throw new IllegalArgumentException("O usuário do admin é obrigatório");
        }
        return repository.save(admin);
    }

}
