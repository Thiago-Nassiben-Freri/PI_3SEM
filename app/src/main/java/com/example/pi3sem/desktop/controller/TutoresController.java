package com.example.pi3sem.desktop.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.pi3sem.desktop.model.Telefone;
import com.example.pi3sem.desktop.model.Tutor;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Tela de listagem de tutores: busca, novo, editar e excluir.
public class TutoresController {

    @FXML
    private TextField txtBusca;
    @FXML
    private TableView<Tutor> tabela;
    @FXML
    private TableColumn<Tutor, String> colNome;
    @FXML
    private TableColumn<Tutor, String> colCpf;
    @FXML
    private TableColumn<Tutor, String> colEmail;
    @FXML
    private TableColumn<Tutor, String> colTelefone;
    @FXML
    private Label lblMensagem;

    // Guarda o primeiro telefone de cada tutor (chave = idTutor)
    private Map<Integer, String> telefones = new HashMap<>();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // O telefone não está no Tutor, então buscamos no mapa pelo id
        colTelefone.setCellValueFactory(c -> {
            String numero = telefones.get(c.getValue().getIdTutor());
            if (numero == null) {
                numero = "";
            }
            return new SimpleStringProperty(numero);
        });

        carregarTutores();
    }

    @FXML
    private void buscar() {
        carregarTutores();
    }

    @FXML
    private void novo() {
        abrirFormulario(null);
    }

    @FXML
    private void editar() {
        Tutor selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblMensagem.setText("Selecione um tutor na tabela.");
            return;
        }
        abrirFormulario(selecionado);
    }

    @FXML
    private void excluir() {
        Tutor selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblMensagem.setText("Selecione um tutor na tabela.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Excluir tutor");
        alerta.setHeaderText("Deseja excluir o tutor " + selecionado.getNome() + "?");
        Optional<ButtonType> resposta = alerta.showAndWait();

        if (resposta.isEmpty() || resposta.get() != ButtonType.OK) {
            return;
        }

        lblMensagem.setText("");
        new Thread(() -> {
            try {
                ApiService.delete("/tutores/" + selecionado.getIdTutor());
                Platform.runLater(() -> carregarTutores());
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Busca os tutores na API (com ou sem filtro de nome) e preenche a tabela
    private void carregarTutores() {
        lblMensagem.setText("");
        String caminho = "/tutores";
        String busca = txtBusca.getText().trim();
        if (!busca.isEmpty()) {
            caminho = caminho + "?busca=" + URLEncoder.encode(busca, StandardCharsets.UTF_8);
        }

        String caminhoFinal = caminho;
        new Thread(() -> {
            try {
                String json = ApiService.get(caminhoFinal);
                Tutor[] tutores = ApiService.getMapper().readValue(json, Tutor[].class);

                // Mostra a lista logo, sem esperar os telefones
                Platform.runLater(() -> tabela.getItems().setAll(tutores));

                // Depois busca o telefone de cada tutor
                Map<Integer, String> novosTelefones = new HashMap<>();
                for (Tutor t : tutores) {
                    String jsonTel = ApiService.get("/tutores/" + t.getIdTutor() + "/telefones");
                    Telefone[] lista = ApiService.getMapper().readValue(jsonTel, Telefone[].class);
                    if (lista.length > 0) {
                        novosTelefones.put(t.getIdTutor(), lista[0].getNumero());
                    }
                }
                Platform.runLater(() -> {
                    telefones = novosTelefones;
                    tabela.refresh(); // redesenha a coluna de telefone
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Abre o formulário em pop-up. tutor = null significa novo cadastro
    private void abrirFormulario(Tutor tutor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutor-form.fxml"));
            Stage janela = new Stage();
            if (tutor == null) {
                janela.setTitle("Novo tutor");
            } else {
                janela.setTitle("Editar tutor");
            }
            janela.initModality(Modality.APPLICATION_MODAL); // trava a tela de trás até fechar
            janela.setScene(new Scene(loader.load()));

            TutorFormController form = loader.getController();
            form.setTutor(tutor);

            janela.showAndWait();
            carregarTutores(); // recarrega a lista depois de fechar
        } catch (Exception e) {
            lblMensagem.setText("Não foi possível abrir o formulário.");
        }
    }
}