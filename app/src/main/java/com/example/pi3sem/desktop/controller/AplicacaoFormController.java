package com.example.pi3sem.desktop.controller;

import java.time.LocalDate;

import com.example.pi3sem.desktop.model.AplicacaoVacina;
import com.example.pi3sem.desktop.model.Pet;
import com.example.pi3sem.desktop.model.Vacina;
import com.example.pi3sem.desktop.model.Veterinario;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Formulário de aplicação de vacina (pop-up). Só cadastra, não edita.
public class AplicacaoFormController {

    @FXML
    private ComboBox<Pet> cmbPet;
    @FXML
    private ComboBox<Vacina> cmbVacina;
    @FXML
    private DatePicker dtAplicacao;
    @FXML
    private TextField txtDose;
    @FXML
    private TextField txtLote;
    @FXML
    private DatePicker dtValidade;
    @FXML
    private TextField txtObservacao;
    @FXML
    private Label lblMensagem;
    @FXML
    private Button btnSalvar;

    @FXML
    public void initialize() {
        dtAplicacao.setValue(LocalDate.now()); // a maioria das aplicações é registrada no mesmo dia
        carregarAnimais();
        carregarVacinas();
    }

    private void carregarAnimais() {
        new Thread(() -> {
            try {
                String json = ApiService.get("/pets");
                Pet[] pets = ApiService.getMapper().readValue(json, Pet[].class);
                Platform.runLater(() -> cmbPet.getItems().setAll(pets));
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    private void carregarVacinas() {
        new Thread(() -> {
            try {
                String json = ApiService.get("/vacinas");
                Vacina[] vacinas = ApiService.getMapper().readValue(json, Vacina[].class);
                Platform.runLater(() -> cmbVacina.getItems().setAll(vacinas));
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void salvar() {
        lblMensagem.setText("");

        Pet petEscolhido = cmbPet.getValue();
        Vacina vacinaEscolhida = cmbVacina.getValue();
        LocalDate dataAplicacao = dtAplicacao.getValue();

        if (petEscolhido == null) {
            lblMensagem.setText("Selecione o animal.");
            return;
        }
        if (vacinaEscolhida == null) {
            lblMensagem.setText("Selecione a vacina.");
            return;
        }
        if (dataAplicacao == null) {
            lblMensagem.setText("Informe a data da aplicação.");
            return;
        }

        // Se a dose ficar vazia, a API avisa que é obrigatória
        Integer dose = null;
        String textoDose = txtDose.getText().trim();
        if (!textoDose.isEmpty()) {
            try {
                dose = Integer.parseInt(textoDose);
            } catch (NumberFormatException e) {
                lblMensagem.setText("Número da dose inválido. Digite só números, por exemplo 1.");
                return;
            }
        }

        // O animal e a vacina vão só com o id preenchido
        Pet petComId = new Pet();
        petComId.setIdPet(petEscolhido.getIdPet());
        Vacina vacinaComId = new Vacina();
        vacinaComId.setIdVacina(vacinaEscolhida.getIdVacina());

        // O veterinário é o usuário que fez login
        Veterinario veterinario = new Veterinario();
        veterinario.setId(ApiService.getUsuarioLogado().getIdUsuario());

        // Monta a aplicação só com os dados da tela
        AplicacaoVacina dados = new AplicacaoVacina();
        dados.setPet(petComId);
        dados.setVacina(vacinaComId);
        dados.setVeterinario(veterinario);
        dados.setDataAplicacao(dataAplicacao.atStartOfDay()); // a API espera data e hora
        dados.setNumeroDose(dose);
        dados.setLote(txtLote.getText().trim());
        dados.setValidadeLote(dtValidade.getValue());
        dados.setObservacao(txtObservacao.getText().trim());

        btnSalvar.setDisable(true); // evita clicar duas vezes

        new Thread(() -> {
            try {
                ApiService.post("/aplicacoes", dados);
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