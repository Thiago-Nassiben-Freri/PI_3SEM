package com.example.pi3sem.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.pi3sem.backend.model.Vacina;
import com.example.pi3sem.backend.repository.VacinaEspecieRepository;
import com.example.pi3sem.backend.repository.VacinaRepository;

class VacinaServiceTest {

    @Test
    void deveRejeitarVacinaDuplicadaMesmasChavesDeNegocio() {
        VacinaRepository vacinaRepository = mock(VacinaRepository.class);
        VacinaEspecieRepository vacinaEspecieRepository = mock(VacinaEspecieRepository.class);
        VacinaService vacinaService = new VacinaService(vacinaRepository, vacinaEspecieRepository);

        Vacina primeira = new Vacina();
        primeira.setIdVacina(10);
        primeira.setNome("Raiva");
        primeira.setTipo("contra raiva");
        primeira.setDosagem("2");
        primeira.setPeriodicidadeDias(365);
        primeira.setFabricante("Teste");

        when(vacinaRepository.findByNomeContainingIgnoreCase("raiva")).thenReturn(List.of(primeira));

        Vacina duplicada = new Vacina();
        duplicada.setNome("raiva");
        duplicada.setTipo("Contra Raiva");
        duplicada.setDosagem("2");
        duplicada.setPeriodicidadeDias(365);
        duplicada.setFabricante("Outro");

        assertThatThrownBy(() -> vacinaService.salvar(duplicada))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Já existe uma vacina cadastrada");
    }

    @Test
    void devePermitirAtualizarMesmaVacinaSemDuplicidade() {
        VacinaRepository vacinaRepository = mock(VacinaRepository.class);
        VacinaEspecieRepository vacinaEspecieRepository = mock(VacinaEspecieRepository.class);
        VacinaService vacinaService = new VacinaService(vacinaRepository, vacinaEspecieRepository);

        Vacina vacina = new Vacina();
        vacina.setIdVacina(7);
        vacina.setNome("Raiva");
        vacina.setTipo("contra raiva");
        vacina.setDosagem("2");
        vacina.setPeriodicidadeDias(365);
        vacina.setFabricante("Teste");

        when(vacinaRepository.findById(7)).thenReturn(java.util.Optional.of(vacina));
        when(vacinaRepository.findByNomeContainingIgnoreCase("raiva")).thenReturn(List.of(vacina));
        when(vacinaRepository.save(vacina)).thenReturn(vacina);

        vacina.setFabricante("Fabricante atualizado");
        Vacina atualizado = vacinaService.atualizar(7, vacina);

        assertThat(atualizado.getFabricante()).isEqualTo("Fabricante atualizado");
    }
}
