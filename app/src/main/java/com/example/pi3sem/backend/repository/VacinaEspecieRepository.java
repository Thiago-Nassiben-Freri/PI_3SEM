package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi3sem.backend.model.VacinaEspecie;
import java.util.List;
import java.util.Optional;

/**
 * Repository para a entidade VacinaEspecie, que representa a relação entre vacinas e espécies de animais.
 * VacinaEspecieRepository
 * List<VacinaEspecie> findByVacina_IdVacina(Integer idVacina): Método para buscar os vínculos de uma vacina.
 * List<VacinaEspecie> findByEspecie_IdEspecie(Integer idEspecie): Método para buscar os vínculos de uma espécie.
 * boolean existsByVacina_IdVacinaAndEspecie_IdEspecie(Integer idVacina, Integer idEspecie): Método para verificar se a vacina já está vinculada à espécie.
 * Optional<VacinaEspecie> findByVacina_IdVacinaAndEspecie_IdEspecie(Integer idVacina, Integer idEspecie): Método para buscar o vínculo entre uma vacina e uma espécie.
 */

public interface VacinaEspecieRepository extends JpaRepository <VacinaEspecie, Long> {
    List<VacinaEspecie> findByVacina_IdVacina(Integer idVacina);

    List<VacinaEspecie> findByEspecie_IdEspecie(Integer idEspecie);

    boolean existsByVacina_IdVacinaAndEspecie_IdEspecie(Integer idVacina, Integer idEspecie);

    Optional<VacinaEspecie> findByVacina_IdVacinaAndEspecie_IdEspecie(Integer idVacina, Integer idEspecie);
}