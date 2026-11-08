package com.example.pi3sem.desktop.controller;

import java.util.Optional;

import com.example.pi3sem.desktop.model.Vacina;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Pop-up com a lista de vacinas do catálogo: novo, editar e excluir.
public class VacinaCatalogoController {
    // Tamanho padrão da janela de cadastro, para todos os campos aparecerem sem cortar
    private static final double LARGURA_FORM = 520;
    private static final double ALTURA_FORM = 650;

    @FXML
    private TableView<Vacina> tabela;
    @FXML
    private TableColumn<Vacina, String> colNome;
    @FXML
    private TableColumn<Vacina, String> colTipo;
    @FXML
    private TableColumn<Vacina, String> colDosagem;
    @FXML
    private TableColumn<Vacina, Integer> colPeriodicidade;
    @FXML
    private TableColumn<Vacina, String> colFabricante;
    @FXML
    private Label lblMensagem;

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDosagem.setCellValueFactory(new PropertyValueFactory<>("dosagem"));
        colPeriodicidade.setCellValueFactory(new PropertyValueFactory<>("periodicidadeDias"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));

        carregarVacinas();
    }

    @FXML
    private void novo() {
        abrirFormulario(null);
    }

    @FXML
    private void editar() {
        Vacina selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            lblMensagem.setText("Selecione uma vacina na tabela.");
            return;
        }
        abrirFormulario(selecionada);
    }

    @FXML
    private void excluir() {
        Vacina selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            lblMensagem.setText("Selecione uma vacina na tabela.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Excluir vacina");
        alerta.setHeaderText("Deseja excluir a vacina " + selecionada.getNome() + "?");
        Optional<ButtonType> resposta = alerta.showAndWait();

        if (resposta.isEmpty() || resposta.get() != ButtonType.OK) {
            return;
        }

        lblMensagem.setText("");
        new Thread(() -> {
            try {
                ApiService.delete("/vacinas/" + selecionada.getIdVacina());
                Platform.runLater(() -> carregarVacinas());
            } catch (Exception e) {
                // Ex.: vacina que já foi aplicada em algum animal pode não ser excluída
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void fechar() {
        ((Stage) tabela.getScene().getWindow()).close();
    }

    // Busca as vacinas na API e preenche a tabela
    private void carregarVacinas() {
        lblMensagem.setText("");
        new Thread(() -> {
            try {
                String json = ApiService.get("/vacinas");
                Vacina[] vacinas = ApiService.getMapper().readValue(json, Vacina[].class);
                Platform.runLater(() -> tabela.getItems().setAll(vacinas));
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Abre o formulário em pop-up. vacina = null significa novo cadastro
    private void abrirFormulario(Vacina vacina) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vacina-form.fxml"));
            Stage janela = new Stage();
            if (vacina == null) {
                janela.setTitle("Nova vacina");
            } else {
                janela.setTitle("Editar vacina");
            }
            janela.initModality(Modality.APPLICATION_MODAL); // trava a tela de trás até fechar
            janela.setScene(new Scene(loader.load(), LARGURA_FORM, ALTURA_FORM));

            VacinaFormController form = loader.getController();
            form.setVacina(vacina);

            janela.showAndWait();
            carregarVacinas(); // recarrega a lista depois de fechar
        } catch (Exception e) {
            lblMensagem.setText("Não foi possível abrir o formulário.");
        }
    }
}