package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Admin;

/**
 * Repository para a entidade Admin.
 * AdminRepository
 */

public interface AdminRepository extends JpaRepository<Admin, Integer> {   
}