package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Veterinario;

public interface VeterinarioRepository extends JpaRepository <Veterinario, Integer> {
}