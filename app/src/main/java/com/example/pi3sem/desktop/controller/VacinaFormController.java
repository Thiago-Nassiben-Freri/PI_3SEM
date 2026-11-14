package com.example.pi3sem.desktop.controller;

import java.util.Arrays;

import com.example.pi3sem.desktop.model.Especie;
import com.example.pi3sem.desktop.model.Vacina;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Formulário de vacina do catálogo (pop-up): serve para cadastrar e para editar.
public class VacinaFormController {

    @FXML
    private Label lblTitulo;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtTipo;
    @FXML
    private ComboBox<Especie> cmbEspecie;
    @FXML
    private TextField txtDosagem;
    @FXML
    private TextField txtPeriodicidade;
    @FXML
    private TextField txtFabricante;
    @FXML
    private Label lblMensagem;
    @FXML
    private Button btnSalvar;

    // null = novo cadastro; preenchido = edição
    private Vacina vacina;

    @FXML
    private void initialize() {
        carregarEspecies();
    }

    // Chamado pelo catálogo logo depois de carregar o formulário
    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
        if (vacina == null) {
            return;
        }

        lblTitulo.setText("Editar vacina");
        txtNome.setText(vacina.getNome());
        txtTipo.setText(vacina.getTipo());
        txtDosagem.setText(vacina.getDosagem());
        if (vacina.getPeriodicidadeDias() != null) {
            txtPeriodicidade.setText(String.valueOf(vacina.getPeriodicidadeDias()));
        }
        txtFabricante.setText(vacina.getFabricante());
        carregarEspeciesVinculadas();
    }

    @FXML
    private void salvar() {
        lblMensagem.setText("");

        if (cmbEspecie.getValue() == null) {
            lblMensagem.setText("Selecione uma espécie para a vacina.");
            return;
        }

        // Se a periodicidade ficar vazia, a API avisa que é obrigatória
        Integer periodicidade = null;
        String textoPeriodicidade = txtPeriodicidade.getText().trim();
        if (!textoPeriodicidade.isEmpty()) {
            try {
                periodicidade = Integer.parseInt(textoPeriodicidade);
            } catch (NumberFormatException e) {
                lblMensagem.setText("Periodicidade inválida. Digite só números, por exemplo 365.");
                return;
            }
        }

        // Monta uma vacina nova só com os dados da tela
        Vacina dados = new Vacina();
        dados.setNome(txtNome.getText().trim());
        dados.setTipo(txtTipo.getText().trim());
        dados.setDosagem(txtDosagem.getText().trim());
        dados.setPeriodicidadeDias(periodicidade);
        dados.setFabricante(txtFabricante.getText().trim());
        if (vacina != null) {
            dados.setIdVacina(vacina.getIdVacina());
        }

        btnSalvar.setDisable(true); // evita clicar duas vezes

        new Thread(() -> {
            try {
                Vacina vacinaSalva;
                if (vacina == null) {
                    String resposta = ApiService.post("/vacinas", dados);
                    vacinaSalva = ApiService.getMapper().readValue(resposta, Vacina.class);
                } else {
                    String resposta = ApiService.put("/vacinas/" + vacina.getIdVacina(), dados);
                    vacinaSalva = ApiService.getMapper().readValue(resposta, Vacina.class);
                }

                vincularEspecieSeNecessario(vacinaSalva.getIdVacina(), cmbEspecie.getValue().getIdEspecie());
                Platform.runLater(() -> fecharJanela());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblMensagem.setText(e.getMessage());
                    btnSalvar.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void carregarEspecies() {
        new Thread(() -> {
            try {
                String json = ApiService.get("/especies");
                Especie[] especies = ApiService.getMapper().readValue(json, Especie[].class);
                Platform.runLater(() -> {
                    cmbEspecie.getItems().setAll(especies);
                    if (vacina != null) {
                        carregarEspeciesVinculadas();
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText("Não foi possível carregar as espécies."));
            }
        }).start();
    }

    private void carregarEspeciesVinculadas() {
        if (vacina == null) {
            return;
        }

        new Thread(() -> {
            try {
                String json = ApiService.get("/vacinas/" + vacina.getIdVacina() + "/especies");
                Especie[] especies = ApiService.getMapper().readValue(json, Especie[].class);
                Platform.runLater(() -> {
                    if (especies.length > 0) {
                        cmbEspecie.setValue(Arrays.stream(cmbEspecie.getItems().toArray(new Especie[0]))
                                .filter(e -> e.getIdEspecie().equals(especies[0].getIdEspecie()))
                                .findFirst()
                                .orElse(null));
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText("Não foi possível carregar a espécie vinculada."));
            }
        }).start();
    }

    private void vincularEspecieSeNecessario(Integer idVacina, Integer idEspecie) throws Exception {
        String json = ApiService.get("/vacinas/" + idVacina + "/especies");
        Especie[] especies = ApiService.getMapper().readValue(json, Especie[].class);
        boolean jaVinculada = Arrays.stream(especies)
                .anyMatch(especie -> especie.getIdEspecie().equals(idEspecie));

        if (!jaVinculada) {
            ApiService.post("/vacinas/" + idVacina + "/especies/" + idEspecie, null);
        }
    }

    private void fecharJanela() {
        ((Stage) btnSalvar.getScene().getWindow()).close();
    }
}