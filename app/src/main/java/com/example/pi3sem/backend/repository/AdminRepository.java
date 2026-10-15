package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Admin;
import java.util.List;

/**
 * Repository para a entidade Admin.
 * AdminRepository
 * List<Admin> findByUsuario_IdUsuario(Integer idUsuario): Método para buscar todos os administradores por ID de Usuário.
 */

public interface AdminRepository extends JpaRepository<Admin, Integer> { 
    List <Admin> findByUsuario_IdUsuario(Integer idUsuario);
}