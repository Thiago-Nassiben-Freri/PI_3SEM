package com.example.pi3sem.desktop.controller;

import com.example.pi3sem.desktop.model.Especie;
import com.example.pi3sem.desktop.model.Pet;
import com.example.pi3sem.desktop.model.Raca;
import com.example.pi3sem.desktop.model.Tutor;
import com.example.pi3sem.desktop.service.ApiService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Formulário de animal (pop-up): serve para cadastrar e para editar.
public class AnimalFormController {

    @FXML
    private Label lblTitulo;
    @FXML
    private TextField txtNome;
    @FXML
    private ComboBox<String> cmbSexo;
    @FXML
    private DatePicker dtNascimento;
    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtObservacoes;
    @FXML
    private ComboBox<Tutor> cmbTutor;
    @FXML
    private ComboBox<Especie> cmbEspecie;
    @FXML
    private ComboBox<Raca> cmbRaca;
    @FXML
    private Label lblMensagem;
    @FXML
    private Button btnSalvar;

    // null = novo cadastro; preenchido = edição
    private Pet pet;

    // Na edição, a raça do animal só pode ser selecionada depois que a lista de raças carregar
    private Raca racaParaSelecionar;

    @FXML
    public void initialize() {
        cmbSexo.getItems().addAll("M", "F");

        // Quando a espécie muda, limpa as raças e carrega as da nova espécie
        cmbEspecie.valueProperty().addListener((obs, antiga, nova) -> {
            cmbRaca.getItems().clear();
            if (nova != null) {
                carregarRacas(nova.getIdEspecie());
            }
        });

        carregarTutores();
        carregarEspecies();
    }

    // Chamado pela tela de animais logo depois de carregar o formulário
    public void setPet(Pet pet) {
        this.pet = pet;
        if (pet == null) {
            return;
        }

        lblTitulo.setText("Editar animal");
        txtNome.setText(pet.getNome());
        cmbSexo.setValue(pet.getSexo());
        dtNascimento.setValue(pet.getDataNascimento());
        if (pet.getPeso() != null) {
            txtPeso.setText(String.valueOf(pet.getPeso()));
        }
        txtObservacoes.setText(pet.getObservacoes());

        racaParaSelecionar = pet.getRaca();

        // As listas dos ComboBox podem já ter carregado ou não; tenta agora e de novo ao carregar
        selecionarTutor();
        selecionarEspecie();
    }

    private void carregarTutores() {
        new Thread(() -> {
            try {
                String json = ApiService.get("/tutores");
                Tutor[] tutores = ApiService.getMapper().readValue(json, Tutor[].class);
                Platform.runLater(() -> {
                    cmbTutor.getItems().setAll(tutores);
                    selecionarTutor();
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    private void carregarEspecies() {
        new Thread(() -> {
            try {
                String json = ApiService.get("/especies");
                Especie[] especies = ApiService.getMapper().readValue(json, Especie[].class);
                Platform.runLater(() -> {
                    cmbEspecie.getItems().setAll(especies);
                    selecionarEspecie();
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    private void carregarRacas(Integer idEspecie) {
        new Thread(() -> {
            try {
                String json = ApiService.get("/especies/" + idEspecie + "/racas");
                Raca[] racas = ApiService.getMapper().readValue(json, Raca[].class);
                Platform.runLater(() -> {
                    cmbRaca.getItems().setAll(racas);
                    selecionarRaca();
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblMensagem.setText(e.getMessage()));
            }
        }).start();
    }

    // Na edição, marca no ComboBox o tutor que o animal já tem
    private void selecionarTutor() {
        if (pet == null || pet.getTutor() == null) {
            return;
        }
        for (Tutor t : cmbTutor.getItems()) {
            if (t.getIdTutor().equals(pet.getTutor().getIdTutor())) {
                cmbTutor.setValue(t);
            }
        }
    }

    // Na edição, marca a espécie da raça do animal (isso dispara o carregamento das raças)
    private void selecionarEspecie() {
        if (pet == null || pet.getRaca() == null || pet.getRaca().getEspecie() == null) {
            return;
        }
        for (Especie e : cmbEspecie.getItems()) {
            if (e.getIdEspecie().equals(pet.getRaca().getEspecie().getIdEspecie())) {
                cmbEspecie.setValue(e);
            }
        }
    }

    // Na edição, marca a raça do animal quando a lista de raças chegar
    private void selecionarRaca() {
        if (racaParaSelecionar == null) {
            return;
        }
        for (Raca r : cmbRaca.getItems()) {
            if (r.getIdRaca().equals(racaParaSelecionar.getIdRaca())) {
                cmbRaca.setValue(r);
            }
        }
        racaParaSelecionar = null; // só seleciona uma vez
    }

    @FXML
    private void salvar() {
        lblMensagem.setText("");

        Tutor tutorEscolhido = cmbTutor.getValue();
        Raca racaEscolhida = cmbRaca.getValue();
        if (tutorEscolhido == null) {
            lblMensagem.setText("Selecione o tutor.");
            return;
        }
        if (racaEscolhida == null) {
            lblMensagem.setText("Selecione a raça.");
            return;
        }

        // Aceita vírgula ou ponto no peso. Se ficar vazio, a API avisa que é obrigatório
        Double peso = null;
        String textoPeso = txtPeso.getText().trim().replace(",", ".");
        if (!textoPeso.isEmpty()) {
            try {
                peso = Double.parseDouble(textoPeso);
            } catch (NumberFormatException e) {
                lblMensagem.setText("Peso inválido. Digite só números, por exemplo 12,5.");
                return;
            }
        }

        // O tutor e a raça vão só com o id preenchido
        Tutor tutorComId = new Tutor();
        tutorComId.setIdTutor(tutorEscolhido.getIdTutor());
        Raca racaComId = new Raca();
        racaComId.setIdRaca(racaEscolhida.getIdRaca());

        // Monta um animal novo só com os dados da tela
        Pet dados = new Pet();
        dados.setNome(txtNome.getText().trim());
        dados.setSexo(cmbSexo.getValue());
        dados.setDataNascimento(dtNascimento.getValue());
        dados.setPeso(peso);
        dados.setObservacoes(txtObservacoes.getText().trim());
        dados.setTutor(tutorComId);
        dados.setRaca(racaComId);
        if (pet != null) {
            dados.setIdPet(pet.getIdPet());
        }

        btnSalvar.setDisable(true); // evita clicar duas vezes

        new Thread(() -> {
            try {
                if (pet == null) {
                    ApiService.post("/pets", dados);
                } else {
                    ApiService.put("/pets/" + pet.getIdPet(), dados);
                }
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

    private void fecharJanela() {
        ((Stage) btnSalvar.getScene().getWindow()).close();
    }
}