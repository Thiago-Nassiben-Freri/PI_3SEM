package com.example.pi3sem.backend.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import com.example.pi3sem.backend.security.UsuarioAutenticado;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Segurança da API (RN01: só usuários cadastrados acessam o sistema).
 * - Autenticação HTTP Basic, sem sessão: o desktop envia login e senha em toda requisição.
 * - POST /auth/login é público (valida as credenciais na tela de login).
 * - Gestão de administradores, usuários e veterinários é só para ADMIN;
 *   cada usuário pode alterar os próprios dados em PUT /usuarios/{id}.
 * - As demais rotas exigem qualquer usuário logado e ativo.
 */
@Configuration
public class SecurityConfig {

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(basic -> basic.authenticationEntryPoint((request, response, ex) ->
                escreverErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Autenticação necessária: informe login e senha válidos")))
        .exceptionHandling(erros -> erros.accessDeniedHandler((request, response, ex) ->
                escreverErro(response, HttpServletResponse.SC_FORBIDDEN, "Acesso negado: esta operação é restrita a administradores")))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers("/admins/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/usuarios/{id}").access((autenticacao, contexto) ->
                    new AuthorizationDecision(adminOuProprioUsuario(autenticacao.get(), contexto)))
            .requestMatchers("/usuarios/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/veterinarios").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/veterinarios/**").hasRole("ADMIN")
            .anyRequest().authenticated());
    return http.build();
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

private static boolean adminOuProprioUsuario(Authentication autenticacao, RequestAuthorizationContext contexto) {
    if (autenticacao == null || !(autenticacao.getPrincipal() instanceof UsuarioAutenticado usuario)) {
        return false;
    }
    return usuario.isAdmin() || String.valueOf(usuario.getIdUsuario()).equals(contexto.getVariables().get("id"));
}

// Mesmo formato do ErroResposta usado pelo GlobalExceptionHandler
private static void escreverErro(HttpServletResponse response, int status, String mensagem) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{\"status\":" + status + ",\"mensagem\":\"" + mensagem
            + "\",\"timestamp\":\"" + LocalDateTime.now() + "\"}");
}

}
