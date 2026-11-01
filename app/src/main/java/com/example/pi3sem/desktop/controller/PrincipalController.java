package com.example.pi3sem.desktop.controller;

import java.io.IOException;
import java.net.URL;

import com.example.pi3sem.desktop.model.UsuarioLogado;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// Controla o menu lateral: cada botão carrega uma tela no centro da janela
public class PrincipalController {

    private static final String ESTILO_BOTAO = "-fx-background-color: transparent;";
    private static final String ESTILO_BOTAO_ATIVO = "-fx-background-color: #164a3d; -fx-background-radius: 8;";

    @FXML
    private BorderPane painelPrincipal;

    @FXML
    private Label lblUsuario;

    @FXML
    private Button btnInicio;

    @FXML
    private Button btnAnimais;

    @FXML
    private Button btnTutores;

    @FXML
    private Button btnAgenda;

    @FXML
    private Button btnVacinas;

    @FXML
    private Button btnNotificacoes;

    // Roda sozinho quando a tela abre
    @FXML
    public void initialize() {
        UsuarioLogado usuario = ApiService.getUsuarioLogado();
        if (usuario != null) {
            lblUsuario.setText(usuario.getNome());
        }
        abrirInicio();
    }

    @FXML
    public void abrirInicio() {
        abrirTela("inicio", btnInicio);
    }

    @FXML
    public void abrirAnimais() {
        abrirTela("animais", btnAnimais);
    }

    @FXML
    public void abrirTutores() {
        abrirTela("tutores", btnTutores);
    }

    @FXML
    public void abrirAgenda() {
        abrirTela("agenda", btnAgenda);
    }

    @FXML
    public void abrirVacinas() {
        abrirTela("vacinas", btnVacinas);
    }

    @FXML
    public void abrirNotificacoes() {
        abrirTela("notificacoes", btnNotificacoes);
    }

    // Volta para a tela de login
    @FXML
    public void sair() {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            painelPrincipal.getScene().setRoot(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carrega o arquivo /fxml/<nome>.fxml no centro e destaca o botão do menu
    private void abrirTela(String nome, Button botao) {
        destacarBotao(botao);

        URL arquivo = getClass().getResource("/fxml/" + nome + ".fxml");
        if (arquivo == null) {
            // A tela ainda não foi criada
            mostrarAviso("Em construção");
            return;
        }

        try {
            Parent tela = FXMLLoader.load(arquivo);
            painelPrincipal.setCenter(tela);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAviso("Erro ao abrir a tela. Veja o terminal.");
        }
    }

    private void mostrarAviso(String texto) {
        Label aviso = new Label(texto);
        aviso.setTextFill(Color.GRAY);
        aviso.setFont(new Font(20));
        painelPrincipal.setCenter(aviso);
    }

    private void destacarBotao(Button botaoAtivo) {
        btnInicio.setStyle(ESTILO_BOTAO);
        btnAnimais.setStyle(ESTILO_BOTAO);
        btnTutores.setStyle(ESTILO_BOTAO);
        btnAgenda.setStyle(ESTILO_BOTAO);
        btnVacinas.setStyle(ESTILO_BOTAO);
        btnNotificacoes.setStyle(ESTILO_BOTAO);
        botaoAtivo.setStyle(ESTILO_BOTAO_ATIVO);
    }
}