package com.example.pi3sem.desktop.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.pi3sem.desktop.model.AplicacaoVacina;
import com.example.pi3sem.desktop.model.Pet;
import com.example.pi3sem.desktop.model.ProximaDose;
import com.example.pi3sem.desktop.model.Prontuario;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

// Pop-up só de leitura com o prontuário do animal:
// dados do animal, telefones do tutor, histórico de vacinas e próximas doses.
public class AnimalDetalhesController {

    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblDadosPet;
    @FXML
    private Label lblObservacoes;
    @FXML
    private Label lblTutor;
    @FXML
    private Label lblTelefones;
    @FXML
    private Label lblMensagem;

    @FXML
    private TableView<AplicacaoVacina> tabelaHistorico;
    @FXML
    private TableColumn<AplicacaoVacina, String> colHistVacina;
    @FXML
    private TableColumn<AplicacaoVacina, String> colHistDose;
    @FXML
    private TableColumn<AplicacaoVacina, String> colHistData;
    @FXML
    private TableColumn<AplicacaoVacina, String> colHistLote;

    @FXML
    private TableView<ProximaDose> tabelaProximas;
    @FXML
    private TableColumn<ProximaDose, String> colProxVacina;
    @FXML
    private TableColumn<ProximaDose, String> colProxDose;
    @FXML
    private TableColumn<ProximaDose, String> colProxData;
    @FXML
    private TableColumn<ProximaDose, String> colProxSituacao;

    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Histórico de vacinas
        colHistVacina.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getVacina() != null) {
                texto = c.getValue().getVacina().getNome();
            }
            return new SimpleStringProperty(texto);
        });
        colHistDose.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getNumeroDose() != null) {
                texto = String.valueOf(c.getValue().getNumeroDose());
            }
            return new SimpleStringProperty(texto);
        });
        colHistData.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getDataAplicacao() != null) {
                texto = c.getValue().getDataAplicacao().format(formato);
            }
            return new SimpleStringProperty(texto);
        });
        colHistLote.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLote()));

        // Próximas doses
        colProxVacina.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getVacina()));
        colProxDose.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getNumeroDose() != null) {
                texto = String.valueOf(c.getValue().getNumeroDose());
            }
            return new SimpleStringProperty(texto);
        });
        colProxData.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getProximaDose() != null) {
                texto = c.getValue().getProximaDose().format(formato);
            }
            return new SimpleStringProperty(texto);
        });
        colProxSituacao.setCellValueFactory(c -> new SimpleStringProperty(textoSituacao(c.getValue())));
    }

    // Chamado pela tela de animais logo depois de carregar o pop-up
    public void setPet(Pet pet) {
        lblTitulo.setText("Detalhes de " + pet.getNome());

        new Thread(() -> {
            try {
                String json = ApiService.get("/pets/" + pet.getIdPet() + "/prontuario");
                Prontuario prontuario = ApiService.getMapper().readValue(json, Prontuario.class);
                Platform.runLater(() -> preencher(prontuario));
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Coloca os dados do prontuário nos textos e nas tabelas
    private void preencher(Prontuario prontuario) {
        Pet pet = prontuario.getPet();
        if (pet != null) {
            String especie = "";
            String raca = "";
            if (pet.getRaca() != null) {
                raca = pet.getRaca().getNome();
                if (pet.getRaca().getEspecie() != null) {
                    especie = pet.getRaca().getEspecie().getNome();
                }
            }

            String sexo = "";
            if ("M".equals(pet.getSexo())) {
                sexo = "Macho";
            } else if ("F".equals(pet.getSexo())) {
                sexo = "Fêmea";
            }

            String nascimento = "";
            LocalDate data = pet.getDataNascimento();
            if (data != null) {
                nascimento = data.format(formato);
            }

            String peso = "";
            if (pet.getPeso() != null) {
                peso = pet.getPeso() + " kg";
            }

            lblDadosPet.setText("Espécie: " + especie + "   Raça: " + raca + "   Sexo: " + sexo
                    + "\nNascimento: " + nascimento + "   Peso: " + peso);

            String observacoes = pet.getObservacoes();
            if (observacoes == null || observacoes.isEmpty()) {
                observacoes = "Nenhuma";
            }
            lblObservacoes.setText("Observações: " + observacoes);

            if (pet.getTutor() != null) {
                lblTutor.setText("Tutor: " + pet.getTutor().getNome());
            }
        }

        if (prontuario.getTelefonesTutor() == null || prontuario.getTelefonesTutor().isEmpty()) {
            lblTelefones.setText("Telefones: nenhum cadastrado");
        } else {
            lblTelefones.setText("Telefones: " + String.join(", ", prontuario.getTelefonesTutor()));
        }

        if (prontuario.getHistorico() != null) {
            tabelaHistorico.getItems().setAll(prontuario.getHistorico());
        }
        if (prontuario.getProximasDoses() != null) {
            tabelaProximas.getItems().setAll(prontuario.getProximasDoses());
        }
    }

    // Transforma VENCIDA / PROXIMA / EM_DIA em texto para a tela
    private String textoSituacao(ProximaDose dose) {
        String situacao = dose.getSituacao();
        if (situacao == null) {
            return "";
        }
        if (situacao.equals("VENCIDA")) {
            return "Vencida";
        }
        if (situacao.equals("PROXIMA")) {
            return "Próxima (" + dose.getDiasRestantes() + " dias)";
        }
        if (situacao.equals("EM_DIA")) {
            return "Em dia";
        }
        return situacao;
    }

    @FXML
    private void fechar() {
        ((Stage) lblTitulo.getScene().getWindow()).close();
    }
}