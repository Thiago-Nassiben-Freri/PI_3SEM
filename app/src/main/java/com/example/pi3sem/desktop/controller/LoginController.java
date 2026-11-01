package com.example.pi3sem.desktop.controller;

import com.example.pi3sem.desktop.model.UsuarioLogado;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
                UsuarioLogado usuario = ApiService.login(email, senha);
                Platform.runLater(() -> loginRealizado(usuario));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblMensagem.setTextFill(Color.RED);
                    lblMensagem.setText(e.getMessage());
                    btnEntrar.setDisable(false);
                });
            }
        }).start();
    }

    private void loginRealizado(UsuarioLogado usuario) {
        lblMensagem.setText("");
        btnEntrar.setDisable(false);

        // TODO: quando a tela de Visão geral existir, abrir ela aqui
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("PetVax");
        alerta.setHeaderText(null);
        alerta.setContentText("Bem-vindo(a), " + usuario.getNome() + "!");
        alerta.showAndWait();
    }
}