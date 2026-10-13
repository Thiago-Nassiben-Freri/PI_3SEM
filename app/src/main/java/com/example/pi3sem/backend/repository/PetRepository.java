package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer> {

}