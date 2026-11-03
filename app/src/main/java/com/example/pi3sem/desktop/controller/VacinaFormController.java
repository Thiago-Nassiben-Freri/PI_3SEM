package com.example.pi3sem.desktop.controller;

import com.example.pi3sem.desktop.model.Vacina;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Formulário de vacina do catálogo (pop-up): serve para cadastrar e para editar.
public class VacinaFormController {

    @FXML
    private Label lblTitulo;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtTipo;
    @FXML
    private TextField txtDosagem;
    @FXML
    private TextField txtPeriodicidade;
    @FXML
    private TextField txtFabricante;
    @FXML
    private Label lblMensagem;
    @FXML
    private Button btnSalvar;

    // null = novo cadastro; preenchido = edição
    private Vacina vacina;

    // Chamado pelo catálogo logo depois de carregar o formulário
    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
        if (vacina == null) {
            return;
        }

        lblTitulo.setText("Editar vacina");
        txtNome.setText(vacina.getNome());
        txtTipo.setText(vacina.getTipo());
        txtDosagem.setText(vacina.getDosagem());
        if (vacina.getPeriodicidadeDias() != null) {
            txtPeriodicidade.setText(String.valueOf(vacina.getPeriodicidadeDias()));
        }
        txtFabricante.setText(vacina.getFabricante());
    }

    @FXML
    private void salvar() {
        lblMensagem.setText("");

        // Se a periodicidade ficar vazia, a API avisa que é obrigatória
        Integer periodicidade = null;
        String textoPeriodicidade = txtPeriodicidade.getText().trim();
        if (!textoPeriodicidade.isEmpty()) {
            try {
                periodicidade = Integer.parseInt(textoPeriodicidade);
            } catch (NumberFormatException e) {
                lblMensagem.setText("Periodicidade inválida. Digite só números, por exemplo 365.");
                return;
            }
        }

        // Monta uma vacina nova só com os dados da tela
        Vacina dados = new Vacina();
        dados.setNome(txtNome.getText().trim());
        dados.setTipo(txtTipo.getText().trim());
        dados.setDosagem(txtDosagem.getText().trim());
        dados.setPeriodicidadeDias(periodicidade);
        dados.setFabricante(txtFabricante.getText().trim());
        if (vacina != null) {
            dados.setIdVacina(vacina.getIdVacina());
        }

        btnSalvar.setDisable(true); // evita clicar duas vezes

        new Thread(() -> {
            try {
                if (vacina == null) {
                    ApiService.post("/vacinas", dados);
                } else {
                    ApiService.put("/vacinas/" + vacina.getIdVacina(), dados);
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