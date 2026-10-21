package com.example.pi3sem.backend.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.pi3sem.backend.model.Usuario;
import com.example.pi3sem.backend.repository.AdminRepository;
import com.example.pi3sem.backend.repository.UsuarioRepository;
import com.example.pi3sem.backend.repository.VeterinarioRepository;

// Carrega o usuário pelo login para o Spring Security (usado no HTTP Basic e no /auth/login)
@Service
public class PetvaxUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final AdminRepository adminRepository;
    private final VeterinarioRepository veterinarioRepository;

    public PetvaxUserDetailsService(UsuarioRepository usuarioRepository, AdminRepository adminRepository,
            VeterinarioRepository veterinarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.adminRepository = adminRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Override
    public UsuarioAutenticado loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
        List<String> papeis = new ArrayList<>();
        if (adminRepository.existsById(usuario.getIdUsuario())) {
            papeis.add("ADMIN");
        }
        if (veterinarioRepository.existsById(usuario.getIdUsuario())) {
            papeis.add("VETERINARIO");
        }
        return new UsuarioAutenticado(usuario.getIdUsuario(), usuario.getNome(), usuario.getLogin(),
                usuario.getEmail(), usuario.getSenha(), Boolean.TRUE.equals(usuario.getAtivo()), papeis);
    }
}
