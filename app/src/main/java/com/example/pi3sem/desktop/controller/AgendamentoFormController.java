package com.example.pi3sem.desktop.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.example.pi3sem.desktop.model.Agendamento;
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

// Formulário de agendamento: cria um novo (agendamento == null) ou edita um existente
public class AgendamentoFormController {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private Label lblTitulo;

    @FXML
    private ComboBox<Pet> cbAnimal;

    @FXML
    private ComboBox<Vacina> cbVacina;

    @FXML
    private DatePicker dpData;

    @FXML
    private ComboBox<String> cbHora;

    @FXML
    private TextField txtObservacao;

    @FXML
    private Label lblMensagem;

    @FXML
    private Button btnSalvar;

    // Agendamento que está sendo editado (null quando é um novo)
    private Agendamento agendamento;

    @FXML
    public void initialize() {
        // Horários de 08:00 até 18:00, de meia em meia hora
        for (int hora = 8; hora <= 18; hora++) {
            cbHora.getItems().add(String.format("%02d:00", hora));
            if (hora < 18) {
                cbHora.getItems().add(String.format("%02d:30", hora));
            }
        }
        dpData.setValue(LocalDate.now());

        // Quando o animal muda, carrega as vacinas indicadas para a espécie dele
        cbAnimal.setOnAction(e -> carregarVacinas());

        carregarAnimais();
    }

    // Chamado pela tela Agenda logo depois de abrir o formulário
    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
        if (agendamento != null) {
            lblTitulo.setText("Editar agendamento");
            dpData.setValue(agendamento.getDataHora().toLocalDate());
            cbHora.setValue(agendamento.getDataHora().format(FORMATO_HORA));
            txtObservacao.setText(agendamento.getObservacao());
        }
    }

    private void carregarAnimais() {
        new Thread(() -> {
            try {
                String json = ApiService.get("/pets");
                Pet[] pets = ApiService.getMapper().readValue(json, Pet[].class);
                Platform.runLater(() -> {
                    cbAnimal.getItems().setAll(pets);
                    // Na edição, já deixa selecionado o animal do agendamento
                    if (agendamento != null) {
                        for (Pet pet : pets) {
                            if (pet.getIdPet().equals(agendamento.getPet().getIdPet())) {
                                cbAnimal.setValue(pet);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    private void carregarVacinas() {
        Pet pet = cbAnimal.getValue();
        if (pet == null) {
            return;
        }
        cbVacina.getItems().clear();
        cbVacina.setPromptText("Carregando...");
        Integer idEspecie = pet.getRaca().getEspecie().getIdEspecie();

        new Thread(() -> {
            try {
                String json = ApiService.get("/especies/" + idEspecie + "/vacinas");
                Vacina[] vacinas = ApiService.getMapper().readValue(json, Vacina[].class);
                Platform.runLater(() -> {
                    cbVacina.getItems().setAll(vacinas);
                    cbVacina.setPromptText("Escolha a vacina");
                    // Na edição, já deixa selecionada a vacina do agendamento
                    if (agendamento != null && agendamento.getVacina() != null) {
                        for (Vacina vacina : vacinas) {
                            if (vacina.getIdVacina().equals(agendamento.getVacina().getIdVacina())) {
                                cbVacina.setValue(vacina);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void salvar() {
        Pet pet = cbAnimal.getValue();
        Vacina vacina = cbVacina.getValue();
        LocalDate data = dpData.getValue();
        String hora = cbHora.getValue();

        if (pet == null || vacina == null || data == null || hora == null) {
            lblMensagem.setText("Preencha animal, vacina, data e horário.");
            return;
        }

        Agendamento dados = new Agendamento();
        dados.setPet(pet);
        dados.setVacina(vacina);
        dados.setDataHora(LocalDateTime.of(data, LocalTime.parse(hora)));
        dados.setObservacao(txtObservacao.getText());

        // Novo agendamento fica com o usuário logado; na edição mantém o veterinário original
        if (agendamento == null) {
            Veterinario veterinario = new Veterinario();
            veterinario.setId(ApiService.getUsuarioLogado().getIdUsuario());
            dados.setVeterinario(veterinario);
        } else {
            dados.setVeterinario(agendamento.getVeterinario());
        }

        btnSalvar.setDisable(true);
        lblMensagem.setText("");

        new Thread(() -> {
            try {
                if (agendamento == null) {
                    ApiService.post("/agendamentos", dados);
                } else {
                    ApiService.put("/agendamentos/" + agendamento.getIdAgendamento(), dados);
                }
                Platform.runLater(() -> fechar());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblMensagem.setText(e.getMessage());
                    btnSalvar.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    public void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage janela = (Stage) btnSalvar.getScene().getWindow();
        janela.close();
    }
}