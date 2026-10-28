package com.example.pi3sem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.pi3sem.backend.model.AplicacaoVacina;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para a entidade AplicacaoVacina.
 * AplicacaoVacinaRepository
 * List<AplicacaoVacina> findByPet_IdPet(Integer idPet): Método para buscar aplicações de vacina por ID do pet.
 * List<AplicacaoVacina> findByPet_IdPetOrderByDataAplicacaoDesc(Integer idPet): Histórico do pet, mais recente primeiro.
 * List<AplicacaoVacina> filtrar(...): Método para buscar aplicações por pet, vacina e/ou período.
 * List<AplicacaoVacina> buscarUltimasAplicacoes(): Última aplicação de cada vacina em cada pet.
 * List<AplicacaoVacina> buscarUltimasAplicacoesDoPet(Integer idPet): Mesmo cálculo, para um pet.
 */

public interface AplicacaoVacinaRepository extends JpaRepository<AplicacaoVacina, Integer> {
    List<AplicacaoVacina> findByPet_IdPet(Integer idPet);

    List<AplicacaoVacina> findByPet_IdPetOrderByDataAplicacaoDesc(Integer idPet);

    // inicio e fim são sempre informados pelo service; petId e vacinaId nulos não filtram
    @Query("""
            SELECT a FROM AplicacaoVacina a
            WHERE a.dataAplicacao BETWEEN :inicio AND :fim
              AND (:petId IS NULL OR a.pet.idPet = :petId)
              AND (:vacinaId IS NULL OR a.vacina.idVacina = :vacinaId)
            ORDER BY a.dataAplicacao DESC
            """)
    List<AplicacaoVacina> filtrar(@Param("inicio") LocalDateTime inicio,
                                  @Param("fim") LocalDateTime fim,
                                  @Param("petId") Integer petId,
                                  @Param("vacinaId") Integer vacinaId);

    @Query("""
            SELECT a FROM AplicacaoVacina a
            WHERE a.dataAplicacao = (SELECT MAX(b.dataAplicacao) FROM AplicacaoVacina b
                                     WHERE b.pet = a.pet AND b.vacina = a.vacina)
            """)
    List<AplicacaoVacina> buscarUltimasAplicacoes();

    @Query("""
            SELECT a FROM AplicacaoVacina a
            WHERE a.pet.idPet = :idPet
              AND a.dataAplicacao = (SELECT MAX(b.dataAplicacao) FROM AplicacaoVacina b
                                     WHERE b.pet = a.pet AND b.vacina = a.vacina)
            """)
    List<AplicacaoVacina> buscarUltimasAplicacoesDoPet(@Param("idPet") Integer idPet);
}
