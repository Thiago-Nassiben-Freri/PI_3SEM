package com.example.pi3sem.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.pi3sem.backend.model.Admin;
import com.example.pi3sem.backend.model.Usuario;
import com.example.pi3sem.backend.repository.AdminRepository;
import com.example.pi3sem.backend.repository.UsuarioRepository;
import com.example.pi3sem.backend.service.AdminService;

/**
 * Cria o primeiro administrador quando o banco ainda não tem nenhum, usando as variáveis
 * de ambiente PETVAX_ADMIN_LOGIN, PETVAX_ADMIN_SENHA (e opcionalmente PETVAX_ADMIN_NOME,
 * PETVAX_ADMIN_EMAIL). Sem isso ninguém conseguiria entrar, já que as rotas exigem login.
 * Se já existir um usuário com esse login, ele é promovido a administrador (a senha não muda).
 */
@Component
public class AdminInicial implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInicial.class);

    private final AdminRepository adminRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdminService adminService;
    private final String login;
    private final String senha;
    private final String nome;
    private final String email;

    public AdminInicial(AdminRepository adminRepository, UsuarioRepository usuarioRepository,
            AdminService adminService,
            @Value("${petvax.admin.login:}") String login,
            @Value("${petvax.admin.senha:}") String senha,
            @Value("${petvax.admin.nome:Administrador}") String nome,
            @Value("${petvax.admin.email:}") String email) {
        this.adminRepository = adminRepository;
        this.usuarioRepository = usuarioRepository;
        this.adminService = adminService;
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.email = email;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.count() > 0) {
            return;
        }
        if (login.isBlank() || senha.isBlank()) {
            log.warn("Nenhum administrador cadastrado. Defina PETVAX_ADMIN_LOGIN e PETVAX_ADMIN_SENHA "
                    + "para criar o primeiro administrador ao iniciar o sistema.");
            return;
        }
        try {
            Usuario usuario = usuarioRepository.findByLogin(login).orElseGet(() -> {
                Usuario novo = new Usuario();
                novo.setNome(nome);
                novo.setLogin(login);
                novo.setSenha(senha);
                novo.setEmail(email.isBlank() ? login + "@petvax.local" : email);
                return novo;
            });
            Admin admin = new Admin();
            admin.setUsuario(usuario);
            adminService.salvar(admin);
            log.info("Administrador inicial '{}' criado.", login);
        } catch (RuntimeException ex) {
            log.error("Não foi possível criar o administrador inicial: {}", ex.getMessage());
        }
    }
}
