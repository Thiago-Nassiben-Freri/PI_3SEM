package com.example.pi3sem.desktop.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.example.pi3sem.desktop.model.Agendamento;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Tela Agenda: lista os agendamentos do dia escolhido
public class AgendaController {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FORMATO_API = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @FXML
    private DatePicker dpData;

    @FXML
    private TableView<Agendamento> tabela;

    @FXML
    private TableColumn<Agendamento, String> colHora;

    @FXML
    private TableColumn<Agendamento, String> colAnimal;

    @FXML
    private TableColumn<Agendamento, String> colTutor;

    @FXML
    private TableColumn<Agendamento, String> colVacina;

    @FXML
    private TableColumn<Agendamento, String> colStatus;

    @FXML
    private TableColumn<Agendamento, String> colObservacao;

    @FXML
    private Label lblMensagem;

    @FXML
    public void initialize() {
        // Os dados de animal, tutor e vacina estão "dentro" do agendamento, por isso cada
        // coluna monta o seu texto
        colHora.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDataHora().format(FORMATO_HORA)));
        colAnimal.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPet().getNome()));
        colTutor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPet().getTutor().getNome()));
        colVacina.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getVacina().getNome()));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(textoStatus(c.getValue().getStatus())));
        colObservacao.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getObservacao()));
        tabela.setPlaceholder(new Label("Nenhum agendamento neste dia."));

        // Sempre que o dia mudar, recarrega a lista
        dpData.setValue(LocalDate.now());
        dpData.valueProperty().addListener((obs, antigo, novo) -> carregarAgendamentos());

        carregarAgendamentos();
    }

    @FXML
    public void diaAnterior() {
        dpData.setValue(dpData.getValue().minusDays(1));
    }

    @FXML
    public void hoje() {
        dpData.setValue(LocalDate.now());
    }

    @FXML
    public void proximoDia() {
        dpData.setValue(dpData.getValue().plusDays(1));
    }

    private void carregarAgendamentos() {
        LocalDate dia = dpData.getValue();
        if (dia == null) {
            return;
        }
        // A API filtra por período: do início ao fim do dia escolhido
        String inicio = dia.atStartOfDay().format(FORMATO_API);
        String fim = dia.atTime(23, 59, 59).format(FORMATO_API);
        lblMensagem.setText("");

        new Thread(() -> {
            try {
                String json = ApiService.get("/agendamentos?inicio=" + inicio + "&fim=" + fim);
                Agendamento[] agendamentos = ApiService.getMapper().readValue(json, Agendamento[].class);
                Platform.runLater(() -> tabela.getItems().setAll(agendamentos));
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void novo() {
        abrirFormulario(null);
    }

    @FXML
    public void editar() {
        Agendamento selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblMensagem.setText("Selecione um agendamento na tabela.");
            return;
        }
        abrirFormulario(selecionado);
    }

    @FXML
    public void confirmar() {
        alterarStatus("CONFIRMADO");
    }

    @FXML
    public void cancelar() {
        if (tabela.getSelectionModel().getSelectedItem() == null) {
            lblMensagem.setText("Selecione um agendamento na tabela.");
            return;
        }
        if (perguntar("Deseja cancelar este agendamento?")) {
            alterarStatus("CANCELADO");
        }
    }

    @FXML
    public void excluir() {
        Agendamento selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblMensagem.setText("Selecione um agendamento na tabela.");
            return;
        }
        if (!perguntar("Deseja excluir este agendamento?")) {
            return;
        }

        new Thread(() -> {
            try {
                ApiService.delete("/agendamentos/" + selecionado.getIdAgendamento());
                Platform.runLater(() -> carregarAgendamentos());
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Troca o status do agendamento selecionado (CONFIRMADO ou CANCELADO)
    private void alterarStatus(String novoStatus) {
        Agendamento selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            lblMensagem.setText("Selecione um agendamento na tabela.");
            return;
        }
        Agendamento corpo = new Agendamento();
        corpo.setStatus(novoStatus);

        new Thread(() -> {
            try {
                ApiService.patch("/agendamentos/" + selecionado.getIdAgendamento() + "/status", corpo);
                Platform.runLater(() -> carregarAgendamentos());
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Abre o formulário pop-up; quando ele fechar, recarrega a lista
    private void abrirFormulario(Agendamento agendamento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/agendamento-form.fxml"));
            Stage janela = new Stage();
            janela.setTitle(agendamento == null ? "Novo agendamento" : "Editar agendamento");
            janela.initModality(Modality.APPLICATION_MODAL);
            janela.setScene(new Scene(loader.load()));

            AgendamentoFormController formulario = loader.getController();
            formulario.setAgendamento(agendamento);

            janela.showAndWait();
            carregarAgendamentos();
        } catch (Exception e) {
            e.printStackTrace();
            lblMensagem.setText("Erro ao abrir o formulário.");
        }
    }

    private boolean perguntar(String pergunta) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("PetVax");
        alerta.setHeaderText(null);
        alerta.setContentText(pergunta);
        Optional<ButtonType> resposta = alerta.showAndWait();
        return resposta.isPresent() && resposta.get() == ButtonType.OK;
    }

    private String textoStatus(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "AGENDADO":
                return "Agendado";
            case "CONFIRMADO":
                return "Confirmado";
            case "REALIZADO":
                return "Realizado";
            case "CANCELADO":
                return "Cancelado";
            default:
                return status;
        }
    }
}