package com.example.pi3sem.desktop.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.pi3sem.desktop.model.RelatorioVacinacao;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

// Tela de relatórios: lista as vacinações realizadas, agendadas ou pendentes em um período.
public class RelatoriosController {

    @FXML
    private ComboBox<String> cbSituacao;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFim;
    @FXML
    private Button btnGerar;
    @FXML
    private TableView<RelatorioVacinacao> tabela;
    @FXML
    private TableColumn<RelatorioVacinacao, String> colSituacao;
    @FXML
    private TableColumn<RelatorioVacinacao, String> colData;
    @FXML
    private TableColumn<RelatorioVacinacao, String> colAnimal;
    @FXML
    private TableColumn<RelatorioVacinacao, String> colTutor;
    @FXML
    private TableColumn<RelatorioVacinacao, String> colVacina;
    @FXML
    private TableColumn<RelatorioVacinacao, String> colVeterinario;
    @FXML
    private TableColumn<RelatorioVacinacao, String> colDose;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblMensagem;

    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        cbSituacao.getItems().setAll("Realizadas", "Agendadas", "Pendentes");
        cbSituacao.setValue("Realizadas");

        // Começa mostrando o mês atual
        LocalDate hoje = LocalDate.now();
        dpInicio.setValue(hoje.withDayOfMonth(1));
        dpFim.setValue(hoje.withDayOfMonth(hoje.lengthOfMonth()));

        colSituacao.setCellValueFactory(c -> new SimpleStringProperty(textoSituacao(c.getValue().getSituacao())));
        colData.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getData() != null) {
                texto = c.getValue().getData().format(formato);
            }
            return new SimpleStringProperty(texto);
        });
        colAnimal.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPet()));
        colTutor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTutor()));
        colVacina.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getVacina()));
        colVeterinario.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getVeterinario() != null ? c.getValue().getVeterinario() : ""));
        colDose.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getNumeroDose() != null ? String.valueOf(c.getValue().getNumeroDose()) : ""));

        gerar();
    }

    // Busca o relatório na API com os filtros da tela
    @FXML
    private void gerar() {
        lblMensagem.setText("");
        LocalDate inicio = dpInicio.getValue();
        LocalDate fim = dpFim.getValue();
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            lblMensagem.setText("A data inicial não pode ser depois da data final.");
            return;
        }

        String caminho = "/relatorios/vacinacoes?situacao=" + cbSituacao.getValue().toLowerCase();
        if (inicio != null) {
            caminho += "&inicio=" + inicio;
        }
        if (fim != null) {
            caminho += "&fim=" + fim;
        }

        btnGerar.setDisable(true);
        lblTotal.setText("Carregando...");
        String url = caminho;
        new Thread(() -> {
            try {
                String json = ApiService.get(url);
                RelatorioVacinacao[] linhas = ApiService.getMapper().readValue(json, RelatorioVacinacao[].class);
                Platform.runLater(() -> {
                    tabela.getItems().setAll(linhas);
                    lblTotal.setText(linhas.length + (linhas.length == 1 ? " vacinação" : " vacinações"));
                    btnGerar.setDisable(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    tabela.getItems().clear();
                    lblTotal.setText("");
                    lblMensagem.setText(e.getMessage());
                    btnGerar.setDisable(false);
                });
            }
        }).start();
    }

    // Sem datas: realizadas e agendadas mostram tudo; pendentes mostram até 30 dias à frente
    @FXML
    private void limparDatas() {
        dpInicio.setValue(null);
        dpFim.setValue(null);
        gerar();
    }

    // Transforma REALIZADA / AGENDADA / PENDENTE em texto para a tela
    private String textoSituacao(String situacao) {
        if (situacao == null) {
            return "";
        }
        switch (situacao) {
            case "REALIZADA":
                return "Realizada";
            case "AGENDADA":
                return "Agendada";
            case "PENDENTE":
                return "Pendente";
            default:
                return situacao;
        }
    }
}