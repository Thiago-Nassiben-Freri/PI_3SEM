package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Admin;
import java.util.List;

/**
 * Repository para a entidade Admin.
 * AdminRepository
 * long countByUsuario_AtivoTrue(): Método para contar os administradores com usuário ativo.
 */

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    long countByUsuario_AtivoTrue();
}
