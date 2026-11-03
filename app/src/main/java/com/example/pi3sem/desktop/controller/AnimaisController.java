package com.example.pi3sem.desktop.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import com.example.pi3sem.desktop.model.Especie;
import com.example.pi3sem.desktop.model.Pet;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Tela de listagem de animais: busca por nome, filtro de espécie,
// novo, editar, excluir e detalhes (prontuário).
public class AnimaisController {

    @FXML
    private TextField txtBusca;
    @FXML
    private ComboBox<Especie> cmbEspecie;
    @FXML
    private TableView<Pet> tabela;
    @FXML
    private TableColumn<Pet, String> colNome;
    @FXML
    private TableColumn<Pet, String> colEspecie;
    @FXML
    private TableColumn<Pet, String> colRaca;
    @FXML
    private TableColumn<Pet, String> colIdade;
    @FXML
    private TableColumn<Pet, String> colTutor;
    @FXML
    private Label lblMensagem;

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // Colunas de objetos aninhados: precisam de uma função própria
        colEspecie.setCellValueFactory(c -> {
            Pet pet = c.getValue();
            String texto = "";
            if (pet.getRaca() != null && pet.getRaca().getEspecie() != null) {
                texto = pet.getRaca().getEspecie().getNome();
            }
            return new SimpleStringProperty(texto);
        });
        colRaca.setCellValueFactory(c -> {
            Pet pet = c.getValue();
            String texto = "";
            if (pet.getRaca() != null) {
                texto = pet.getRaca().getNome();
            }
            return new SimpleStringProperty(texto);
        });
        colIdade.setCellValueFactory(c -> new SimpleStringProperty(calcularIdade(c.getValue().getDataNascimento())));
        colTutor.setCellValueFactory(c -> {
            Pet pet = c.getValue();
            String texto = "";
            if (pet.getTutor() != null) {
                texto = pet.getTutor().getNome();
            }
            return new SimpleStringProperty(texto);
        });

        carregarEspecies();
        carregarAnimais();
    }

    @FXML
    private void buscar() {
        carregarAnimais();
    }

    @FXML
    private void novo() {
        abrirFormulario(null);
    }

    @FXML
    private void editar() {
        Pet selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblMensagem.setText("Selecione um animal na tabela.");
            return;
        }
        abrirFormulario(selecionado);
    }

    @FXML
    private void detalhes() {
        Pet selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblMensagem.setText("Selecione um animal na tabela.");
            return;
        }
        abrirDetalhes(selecionado);
    }

    @FXML
    private void excluir() {
        Pet selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblMensagem.setText("Selecione um animal na tabela.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Excluir animal");
        alerta.setHeaderText("Deseja excluir o animal " + selecionado.getNome() + "?");
        Optional<ButtonType> resposta = alerta.showAndWait();

        if (resposta.isEmpty() || resposta.get() != ButtonType.OK) {
            return;
        }

        lblMensagem.setText("");
        new Thread(() -> {
            try {
                ApiService.delete("/pets/" + selecionado.getIdPet());
                Platform.runLater(() -> carregarAnimais());
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Preenche o ComboBox do filtro. O primeiro item, "Todas", significa sem filtro
    private void carregarEspecies() {
        new Thread(() -> {
            try {
                String json = ApiService.get("/especies");
                Especie[] especies = ApiService.getMapper().readValue(json, Especie[].class);

                Especie todas = new Especie();
                todas.setNome("Todas as espécies");

                Platform.runLater(() -> {
                    cmbEspecie.getItems().clear();
                    cmbEspecie.getItems().add(todas);
                    cmbEspecie.getItems().addAll(especies);
                    cmbEspecie.getSelectionModel().selectFirst();
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Busca os animais na API (com os filtros preenchidos) e preenche a tabela
    private void carregarAnimais() {
        lblMensagem.setText("");

        String caminho = "/pets";
        String separador = "?";

        String nome = txtBusca.getText().trim();
        if (!nome.isEmpty()) {
            caminho = caminho + separador + "nome=" + URLEncoder.encode(nome, StandardCharsets.UTF_8);
            separador = "&";
        }

        // "Todas as espécies" não tem id, então só filtra se a espécie for real
        Especie especie = cmbEspecie.getValue();
        if (especie != null && especie.getIdEspecie() != null) {
            caminho = caminho + separador + "especieId=" + especie.getIdEspecie();
        }

        String caminhoFinal = caminho;
        new Thread(() -> {
            try {
                String json = ApiService.get(caminhoFinal);
                Pet[] pets = ApiService.getMapper().readValue(json, Pet[].class);
                Platform.runLater(() -> tabela.getItems().setAll(pets));
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Idade em anos; para filhotes com menos de 1 ano, mostra em meses
    private String calcularIdade(LocalDate nascimento) {
        if (nascimento == null) {
            return "";
        }
        Period periodo = Period.between(nascimento, LocalDate.now());
        int anos = periodo.getYears();
        if (anos >= 1) {
            if (anos == 1) {
                return "1 ano";
            }
            return anos + " anos";
        }
        int meses = periodo.getMonths();
        if (meses == 1) {
            return "1 mês";
        }
        return meses + " meses";
    }

    // Abre o formulário em pop-up. pet = null significa novo cadastro
    private void abrirFormulario(Pet pet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/animal-form.fxml"));
            Stage janela = new Stage();
            if (pet == null) {
                janela.setTitle("Novo animal");
            } else {
                janela.setTitle("Editar animal");
            }
            janela.initModality(Modality.APPLICATION_MODAL); // trava a tela de trás até fechar
            janela.setScene(new Scene(loader.load()));

            AnimalFormController form = loader.getController();
            form.setPet(pet);

            janela.showAndWait();
            carregarAnimais(); // recarrega a lista depois de fechar
        } catch (Exception e) {
            lblMensagem.setText("Não foi possível abrir o formulário.");
        }
    }

    // Abre o pop-up só de leitura com o prontuário do animal
    private void abrirDetalhes(Pet pet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/animal-detalhes.fxml"));
            Stage janela = new Stage();
            janela.setTitle("Detalhes de " + pet.getNome());
            janela.initModality(Modality.APPLICATION_MODAL);
            janela.setScene(new Scene(loader.load()));

            AnimalDetalhesController detalhes = loader.getController();
            detalhes.setPet(pet);

            janela.showAndWait();
        } catch (Exception e) {
            lblMensagem.setText("Não foi possível abrir os detalhes.");
        }
    }
}