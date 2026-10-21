package com.example.pi3sem.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.pi3sem.backend.model.Admin;
import com.example.pi3sem.backend.model.Usuario;
import com.example.pi3sem.backend.repository.AdminRepository;

@Service
public class AdminService {

    private final AdminRepository repository;
    private final UsuarioService usuarioService;

    public AdminService(AdminRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public List<Admin> listar() {
        return repository.findAll();
    }

    // Com usuario.idUsuario promove um usuário existente (ex.: um veterinário);
    // sem idUsuario cria um usuário novo, na mesma transação
    @Transactional
    public Admin salvar(Admin admin) {
        if (admin ==  null) {
            throw new IllegalArgumentException("O admin não pode ser nulo");
        }
        if (admin.getUsuario() == null) {
            throw new IllegalArgumentException("O usuário do admin é obrigatório");
        }
        Usuario usuario = usuarioService.buscarOuCriar(admin.getUsuario());
        if (repository.existsById(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("Este usuário já é administrador");
        }
        admin.setId(null);
        admin.setUsuario(usuario);
        return repository.save(admin);
    }

}
