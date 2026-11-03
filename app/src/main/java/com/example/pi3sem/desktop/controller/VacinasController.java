package com.example.pi3sem.desktop.controller;

import java.time.format.DateTimeFormatter;

import com.example.pi3sem.desktop.model.ProximaDose;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Tela de vacinas: lista as próximas doses ou as vencidas, e abre
// o formulário de aplicação e o catálogo de vacinas.
public class VacinasController {

    @FXML
    private Label lblFiltro;
    @FXML
    private TableView<ProximaDose> tabela;
    @FXML
    private TableColumn<ProximaDose, String> colAnimal;
    @FXML
    private TableColumn<ProximaDose, String> colTutor;
    @FXML
    private TableColumn<ProximaDose, String> colVacina;
    @FXML
    private TableColumn<ProximaDose, String> colDose;
    @FXML
    private TableColumn<ProximaDose, String> colUltima;
    @FXML
    private TableColumn<ProximaDose, String> colProxima;
    @FXML
    private TableColumn<ProximaDose, String> colSituacao;
    @FXML
    private Label lblMensagem;

    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Guarda qual lista está sendo mostrada, para recarregar a mesma depois de salvar
    private String caminhoAtual = "/aplicacoes/proximas-doses";

    @FXML
    public void initialize() {
        colAnimal.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPet()));
        colTutor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTutor()));
        colVacina.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getVacina()));
        colDose.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getNumeroDose() != null) {
                texto = String.valueOf(c.getValue().getNumeroDose());
            }
            return new SimpleStringProperty(texto);
        });
        colUltima.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getUltimaAplicacao() != null) {
                texto = c.getValue().getUltimaAplicacao().format(formato);
            }
            return new SimpleStringProperty(texto);
        });
        colProxima.setCellValueFactory(c -> {
            String texto = "";
            if (c.getValue().getProximaDose() != null) {
                texto = c.getValue().getProximaDose().format(formato);
            }
            return new SimpleStringProperty(texto);
        });
        colSituacao.setCellValueFactory(c -> new SimpleStringProperty(textoSituacao(c.getValue())));

        carregarDoses();
    }

    @FXML
    private void proximas() {
        caminhoAtual = "/aplicacoes/proximas-doses";
        carregarDoses();
    }

    @FXML
    private void vencidas() {
        caminhoAtual = "/aplicacoes/vencidas";
        carregarDoses();
    }

    @FXML
    private void registrarAplicacao() {
        abrirJanela("/fxml/aplicacao-form.fxml", "Registrar aplicação");
    }

    @FXML
    private void abrirCatalogo() {
        abrirJanela("/fxml/vacina-catalogo.fxml", "Catálogo de vacinas");
    }

    // Busca a lista na API e preenche a tabela
    private void carregarDoses() {
        lblMensagem.setText("");
        if (caminhoAtual.equals("/aplicacoes/vencidas")) {
            lblFiltro.setText("Mostrando: doses vencidas");
        } else {
            lblFiltro.setText("Mostrando: próximas doses");
        }

        String caminho = caminhoAtual;
        new Thread(() -> {
            try {
                String json = ApiService.get(caminho);
                ProximaDose[] doses = ApiService.getMapper().readValue(json, ProximaDose[].class);
                Platform.runLater(() -> tabela.getItems().setAll(doses));
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
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

    // Abre um pop-up e, quando ele fechar, recarrega a lista
    private void abrirJanela(String arquivoFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(arquivoFxml));
            Stage janela = new Stage();
            janela.setTitle(titulo);
            janela.initModality(Modality.APPLICATION_MODAL); // trava a tela de trás até fechar
            janela.setScene(new Scene(loader.load()));

            janela.showAndWait();
            carregarDoses();
        } catch (Exception e) {
            lblMensagem.setText("Não foi possível abrir a janela.");
        }
    }
}