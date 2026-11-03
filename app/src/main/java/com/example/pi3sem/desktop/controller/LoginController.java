package com.example.pi3sem.desktop.controller;

import java.io.IOException;

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
import javafx.scene.paint.Color;

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
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Preencha o e-mail e a senha.");
            return;
        }

        lblMensagem.setTextFill(Color.GRAY);
        lblMensagem.setText("Entrando...");
        btnEntrar.setDisable(true);

        // A chamada à API roda em outra thread para a tela não travar enquanto espera.
        // Platform.runLater volta para a thread da tela para poder alterar os componentes.
        new Thread(() -> {
            try {
                ApiService.login(email, senha);
                Platform.runLater(() -> abrirSistema());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblMensagem.setTextFill(Color.RED);
                    lblMensagem.setText(e.getMessage());
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
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Erro ao abrir o sistema. Veja o terminal.");
            btnEntrar.setDisable(false);
        }
    }
}