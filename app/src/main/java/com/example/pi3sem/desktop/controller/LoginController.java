package com.example.pi3sem.desktop.controller;

import java.io.IOException;

import com.example.pi3sem.desktop.Tema;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private TextField txtSenhaVisivel;

    @FXML
    private CheckBox chkMostrarSenha;

    @FXML
    private Label lblMensagem;

    @FXML
    private Button btnEntrar;

    @FXML
    private ToggleButton btnModoEscuro;

    @FXML
    private void initialize() {
        Tema.configurarBotao(btnModoEscuro);
    }

    private void mostrarMensagem(String mensagem, boolean erro) {
        lblMensagem.getStyleClass().removeAll("texto-erro", "texto-secundario");
        lblMensagem.getStyleClass().add(erro ? "texto-erro" : "texto-secundario");
        lblMensagem.setText(mensagem);
    }

    // Troca o campo de senha escondida pelo campo de texto normal (e vice-versa)
    @FXML
    public void mostrarSenha() {
        boolean mostrar = chkMostrarSenha.isSelected();

        if (mostrar) {
            txtSenhaVisivel.setText(txtSenha.getText());
        } else {
            txtSenha.setText(txtSenhaVisivel.getText());
        }

        txtSenhaVisivel.setVisible(mostrar);
        txtSenhaVisivel.setManaged(mostrar);
        txtSenha.setVisible(!mostrar);
        txtSenha.setManaged(!mostrar);
    }

    @FXML
    public void entrar() {
        String email = txtEmail.getText().trim();
        String senha;
        if (chkMostrarSenha.isSelected()) {
            senha = txtSenhaVisivel.getText();
        } else {
            senha = txtSenha.getText();
        }

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarMensagem("Preencha o e-mail e a senha.", true);
            return;
        }

        mostrarMensagem("Entrando...", false);
        btnEntrar.setDisable(true);

        // A chamada à API roda em outra thread para a tela não travar enquanto espera.
        // Platform.runLater volta para a thread da tela para poder alterar os componentes.
        new Thread(() -> {
            try {
                ApiService.login(email, senha);
                Platform.runLater(() -> abrirSistema());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    mostrarMensagem(e.getMessage(), true);
                    btnEntrar.setDisable(false);
                });
            }
        }).start();
    }

    // Troca a tela de login pela estrutura principal (menu lateral + telas)
    private void abrirSistema() {
        try {
            Parent principal = FXMLLoader.load(getClass().getResource("/fxml/principal.fxml"));
            btnEntrar.getScene().setRoot(principal);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensagem("Erro ao abrir o sistema. Veja o terminal.", true);
            btnEntrar.setDisable(false);
        }
    }
}