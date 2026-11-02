package com.example.pi3sem.desktop.controller;

import java.awt.Desktop;
import java.net.URI;
import java.time.format.DateTimeFormatter;

import com.example.pi3sem.desktop.model.EnvioNotificacao;
import com.example.pi3sem.desktop.model.Notificacao;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

// Tela Notificações: lista os lembretes e envia pelo WhatsApp (link wa.me aberto no navegador)
public class NotificacoesController {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private ComboBox<String> cbFiltro;

    @FXML
    private TableView<Notificacao> tabela;

    @FXML
    private TableColumn<Notificacao, String> colData;

    @FXML
    private TableColumn<Notificacao, String> colTutor;

    @FXML
    private TableColumn<Notificacao, String> colTipo;

    @FXML
    private TableColumn<Notificacao, String> colStatus;

    @FXML
    private Label lblDestinatario;

    @FXML
    private Label lblMotivo;

    @FXML
    private TextArea txtMensagem;

    @FXML
    private Button btnEnviar;

    @FXML
    private Label lblMensagem;

    @FXML
    public void initialize() {
        colData.setCellValueFactory(c -> new SimpleStringProperty(textoData(c.getValue())));
        colTutor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTutor().getNome()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(textoTipo(c.getValue().getTipo())));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(textoStatus(c.getValue().getStatus())));
        tabela.setPlaceholder(new Label("Nenhuma notificação."));

        // Ao clicar numa linha, mostra a notificação no painel de envio
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antiga, nova) -> mostrarNotificacao(nova));

        cbFiltro.getItems().addAll("Pendentes", "Enviadas", "Todas");
        cbFiltro.setValue("Pendentes");
        cbFiltro.setOnAction(e -> carregarNotificacoes());

        mostrarNotificacao(null);
        carregarNotificacoes();
    }

    private void carregarNotificacoes() {
        String caminho = "/notificacoes";
        if (cbFiltro.getValue().equals("Pendentes")) {
            caminho = "/notificacoes?status=PENDENTE";
        } else if (cbFiltro.getValue().equals("Enviadas")) {
            caminho = "/notificacoes?status=ENVIADA";
        }
        String caminhoFinal = caminho;

        new Thread(() -> {
            try {
                String json = ApiService.get(caminhoFinal);
                Notificacao[] notificacoes = ApiService.getMapper().readValue(json, Notificacao[].class);
                Platform.runLater(() -> tabela.getItems().setAll(notificacoes));
            } catch (Exception e) {
                Platform.runLater(() -> mostrarErro(e.getMessage()));
            }
        }).start();
    }

    // Pede para a API criar os lembretes de vacinas próximas, atrasadas e agendamentos
    @FXML
    public void gerarLembretes() {
        new Thread(() -> {
            try {
                String json = ApiService.post("/notificacoes/gerar-pendentes", null);
                Notificacao[] criadas = ApiService.getMapper().readValue(json, Notificacao[].class);
                Platform.runLater(() -> {
                    mostrarSucesso(criadas.length + " lembrete(s) novo(s) gerado(s).");
                    carregarNotificacoes();
                });
            } catch (Exception e) {
                Platform.runLater(() -> mostrarErro(e.getMessage()));
            }
        }).start();
    }

    // Preenche o painel da direita (ou limpa, se nenhuma estiver selecionada)
    private void mostrarNotificacao(Notificacao notificacao) {
        if (notificacao == null) {
            lblDestinatario.setText("Selecione uma notificação na lista");
            lblMotivo.setText("-");
            txtMensagem.setText("");
            txtMensagem.setDisable(true);
            btnEnviar.setDisable(true);
            return;
        }
        boolean jaEnviada = "ENVIADA".equals(notificacao.getStatus());

        lblDestinatario.setText(notificacao.getTutor().getNome());
        lblMotivo.setText(textoTipo(notificacao.getTipo()));
        txtMensagem.setText(notificacao.getMensagem());
        txtMensagem.setDisable(jaEnviada);
        btnEnviar.setDisable(jaEnviada);
    }

    @FXML
    public void enviar() {
        Notificacao notificacao = tabela.getSelectionModel().getSelectedItem();
        if (notificacao == null) {
            return;
        }
        String texto = txtMensagem.getText().trim();
        if (texto.isEmpty()) {
            mostrarErro("A mensagem não pode ficar vazia.");
            return;
        }
        boolean textoAlterado = !texto.equals(notificacao.getMensagem());

        btnEnviar.setDisable(true);
        lblMensagem.setText("");

        new Thread(() -> {
            try {
                // 1. Se o texto foi editado, salva antes de enviar
                if (textoAlterado) {
                    Notificacao dados = new Notificacao();
                    dados.setTipo(notificacao.getTipo());
                    dados.setCanal(notificacao.getCanal());
                    dados.setMensagem(texto);
                    ApiService.put("/notificacoes/" + notificacao.getIdNotificacao(), dados);
                }

                // 2. A API marca como enviada e devolve o link do WhatsApp
                String json = ApiService.post("/notificacoes/" + notificacao.getIdNotificacao() + "/enviar", null);
                EnvioNotificacao envio = ApiService.getMapper().readValue(json, EnvioNotificacao.class);

                // 3. Abre o link no navegador: o WhatsApp Web abre com a mensagem pronta
                Desktop.getDesktop().browse(new URI(envio.getLink()));

                Platform.runLater(() -> {
                    mostrarSucesso("WhatsApp aberto. Confira a mensagem e clique em enviar.");
                    carregarNotificacoes();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    mostrarErro(e.getMessage());
                    btnEnviar.setDisable(false);
                });
            }
        }).start();
    }

    private void mostrarErro(String texto) {
        lblMensagem.setTextFill(Color.RED);
        lblMensagem.setText(texto);
    }

    private void mostrarSucesso(String texto) {
        lblMensagem.setTextFill(Color.web("#12805d"));
        lblMensagem.setText(texto);
    }

    private String textoData(Notificacao notificacao) {
        if (notificacao.getDataEnvio() == null) {
            return "";
        }
        return notificacao.getDataEnvio().format(FORMATO_DATA);
    }

    private String textoTipo(String tipo) {
        if (tipo == null) {
            return "";
        }
        switch (tipo) {
            case "PROXIMA_DOSE":
                return "Próxima dose de vacina";
            case "DOSE_ATRASADA":
                return "Vacina atrasada";
            case "LEMBRETE_AGENDAMENTO":
                return "Lembrete de agendamento";
            default:
                return "Outro";
        }
    }

    private String textoStatus(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "PENDENTE":
                return "Pendente";
            case "ENVIADA":
                return "Enviada";
            case "FALHA":
                return "Falha";
            default:
                return status;
        }
    }
}