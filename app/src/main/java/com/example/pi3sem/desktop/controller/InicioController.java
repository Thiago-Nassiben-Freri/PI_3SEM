package com.example.pi3sem.desktop.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.example.pi3sem.desktop.model.Agendamento;
import com.example.pi3sem.desktop.model.Dashboard;
import com.example.pi3sem.desktop.model.ProximaDose;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

// Tela Início: mostra o resumo do dia (GET /dashboard/resumo)
public class InicioController {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FORMATO_DATA_EXTENSO =
            DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("pt-BR"));

    @FXML
    private Label lblData;

    @FXML
    private Label lblTotalPets;

    @FXML
    private Label lblTotalTutores;

    @FXML
    private Label lblDosesProximas;

    @FXML
    private Label lblDosesVencidas;

    @FXML
    private Label lblMensagem;

    @FXML
    private TableView<ProximaDose> tabelaVacinas;

    @FXML
    private TableColumn<ProximaDose, String> colAnimal;

    @FXML
    private TableColumn<ProximaDose, String> colVacina;

    @FXML
    private TableColumn<ProximaDose, String> colProximaDose;

    @FXML
    private TableColumn<ProximaDose, String> colSituacao;

    @FXML
    private ListView<String> listaAgenda;

    @FXML
    public void initialize() {
        // Ex.: "Domingo, 1 de novembro de 2026"
        String hoje = LocalDate.now().format(FORMATO_DATA_EXTENSO);
        lblData.setText(hoje.substring(0, 1).toUpperCase() + hoje.substring(1));

        // Diz à tabela de onde vem o texto de cada coluna
        colAnimal.setCellValueFactory(new PropertyValueFactory<>("pet"));
        colVacina.setCellValueFactory(new PropertyValueFactory<>("vacina"));
        colProximaDose.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProximaDose().format(FORMATO_DATA)));
        colSituacao.setCellValueFactory(c -> new SimpleStringProperty(textoSituacao(c.getValue())));

        // Texto que aparece quando a tabela ou a lista estão vazias
        tabelaVacinas.setPlaceholder(new Label("Nenhuma vacina nos próximos dias."));
        listaAgenda.setPlaceholder(new Label("Nenhum agendamento para hoje."));

        carregarDados();
    }

    private void carregarDados() {
        lblMensagem.setTextFill(Color.GRAY);
        lblMensagem.setText("Carregando...");

        new Thread(() -> {
            try {
                String json = ApiService.get("/dashboard/resumo");
                Dashboard resumo = ApiService.getMapper().readValue(json, Dashboard.class);
                Platform.runLater(() -> mostrarDados(resumo));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblMensagem.setTextFill(Color.RED);
                    lblMensagem.setText(e.getMessage());
                });
            }
        }).start();
    }

    private void mostrarDados(Dashboard resumo) {
        lblMensagem.setText("");

        lblTotalPets.setText(String.valueOf(resumo.getTotalPets()));
        lblTotalTutores.setText(String.valueOf(resumo.getTotalTutores()));
        lblDosesProximas.setText(String.valueOf(resumo.getDosesProximas()));
        lblDosesVencidas.setText(String.valueOf(resumo.getDosesVencidas()));

        if (resumo.getVacinasProximas() != null) {
            tabelaVacinas.getItems().setAll(resumo.getVacinasProximas());
        }

        // Cada agendamento vira uma linha: "09:00  Luna - V10"
        listaAgenda.getItems().clear();
        if (resumo.getCompromissosDoDia() != null) {
            for (Agendamento agendamento : resumo.getCompromissosDoDia()) {
                String texto = agendamento.getDataHora().format(FORMATO_HORA) + "  " + agendamento.getPet().getNome();
                if (agendamento.getVacina() != null) {
                    texto = texto + " - " + agendamento.getVacina().getNome();
                }
                listaAgenda.getItems().add(texto);
            }
        }
    }

    // Transforma a situação da API em um texto amigável
    private String textoSituacao(ProximaDose dose) {
        long dias = dose.getDiasRestantes();
        if (dias < 0) {
            return "Vencida há " + (-dias) + " dia(s)";
        }
        if (dias == 0) {
            return "Vence hoje";
        }
        return "Em " + dias + " dia(s)";
    }
}