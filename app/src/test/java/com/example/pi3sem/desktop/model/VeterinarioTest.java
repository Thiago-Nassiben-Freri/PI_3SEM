package com.example.pi3sem.desktop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VeterinarioTest {

    @Test
    void deveExibirCrmvQuandoDisponivel() {
        Veterinario veterinario = new Veterinario();
        veterinario.setId(12);
        veterinario.setCrmv(4567);

        assertEquals("CRMV 4567", veterinario.toString());
    }

    @Test
    void deveExibirIdQuandoCrmvNaoDisponivel() {
        Veterinario veterinario = new Veterinario();
        veterinario.setId(7);

        assertEquals("Veterinário #7", veterinario.toString());
    }
}
