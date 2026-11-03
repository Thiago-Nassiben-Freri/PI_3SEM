package com.example.pi3sem.desktop.controller;

import java.util.Map;

import com.example.pi3sem.desktop.model.Telefone;
import com.example.pi3sem.desktop.model.Tutor;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Formulário de tutor (pop-up): serve para cadastrar e para editar.
public class TutorFormController {

    @FXML
    private Label lblTitulo;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCep;
    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtTelefone;
    @FXML
    private Label lblMensagem;
    @FXML
    private Button btnSalvar;

    // null = novo cadastro; preenchido = edição
    private Tutor tutor;

    // Telefone que já estava cadastrado (para saber se o usuário mudou)
    private String telefoneOriginal = "";

    // Chamado pela tela de tutores logo depois de carregar o formulário
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
        if (tutor == null) {
            return;
        }

        lblTitulo.setText("Editar tutor");
        txtNome.setText(tutor.getNome());
        txtCpf.setText(tutor.getCpf());
        txtEmail.setText(tutor.getEmail());
        txtCep.setText(tutor.getCep());
        txtNumero.setText(tutor.getNumero());

        // O telefone fica em outra tabela da API, então é buscado à parte
        new Thread(() -> {
            try {
                String json = ApiService.get("/tutores/" + tutor.getIdTutor() + "/telefones");
                Telefone[] lista = ApiService.getMapper().readValue(json, Telefone[].class);
                if (lista.length > 0) {
                    String numero = lista[0].getNumero();
                    Platform.runLater(() -> {
                        telefoneOriginal = numero;
                        txtTelefone.setText(numero);
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void salvar() {
        lblMensagem.setText("");

        // Monta um tutor novo só com os dados da tela
        Tutor dados = new Tutor();
        dados.setNome(txtNome.getText().trim());
        dados.setCpf(txtCpf.getText().trim());
        dados.setEmail(txtEmail.getText().trim());
        dados.setCep(txtCep.getText().trim());
        dados.setNumero(txtNumero.getText().trim());
        if (tutor != null) {
            dados.setIdTutor(tutor.getIdTutor());
        }

        // Deixa só os dígitos do telefone
        String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");

        btnSalvar.setDisable(true); // evita clicar duas vezes

        new Thread(() -> {
            try {
                if (tutor == null) {
                    String json = ApiService.post("/tutores", dados);
                    // Guarda o tutor criado: se o telefone falhar, salvar de novo vira edição
                    tutor = ApiService.getMapper().readValue(json, Tutor.class);
                } else {
                    ApiService.put("/tutores/" + tutor.getIdTutor(), dados);
                }

                // Só envia o telefone se foi preenchido e é diferente do que já existia
                if (!telefone.isEmpty() && !telefone.equals(telefoneOriginal)) {
                    ApiService.post("/tutores/" + tutor.getIdTutor() + "/telefones",
                            Map.of("numero", telefone));
                    telefoneOriginal = telefone;
                }

                Platform.runLater(() -> fecharJanela());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblMensagem.setText(e.getMessage());
                    btnSalvar.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        ((Stage) btnSalvar.getScene().getWindow()).close();
    }
}