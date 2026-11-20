package com.example.pi3sem.desktop;

import java.util.prefs.Preferences;

import javafx.css.PseudoClass;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ToggleButton;

public final class Tema {
    private static final String CSS = Tema.class.getResource("/css/tema.css").toExternalForm();
    private static final PseudoClass ESCURO = PseudoClass.getPseudoClass("escuro");
    private static final String CHAVE = "modoEscuro";
    private static final Preferences PREFERENCIAS = Preferences.userNodeForPackage(Tema.class);
    private static boolean escuro = PREFERENCIAS.getBoolean(CHAVE, false);

    private Tema() {
    }

    public static Scene criarCena(Parent raiz) {
        return configurarCena(new Scene(raiz));
    }

    public static Scene criarCena(Parent raiz, double largura, double altura) {
        return configurarCena(new Scene(raiz, largura, altura));
    }

    private static Scene configurarCena(Scene cena) {
        cena.getStylesheets().add(CSS);
        aplicar(cena.getRoot());
        cena.rootProperty().addListener((obs, anterior, atual) -> aplicar(atual));
        return cena;
    }

    public static void configurarDialogo(DialogPane painel) {
        painel.getStylesheets().add(CSS);
        aplicar(painel);
    }

    public static void configurarBotao(ToggleButton botao) {
        botao.setSelected(escuro);
        botao.setOnAction(evento -> {
            escuro = botao.isSelected();
            aplicar(botao.getScene().getRoot());
            PREFERENCIAS.putBoolean(CHAVE, escuro);
        });
    }

    private static void aplicar(Parent raiz) {
        raiz.pseudoClassStateChanged(ESCURO, escuro);
    }
}
