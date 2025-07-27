package com.poo.petshop.controller;

import com.poo.petshop.dao.AtendenteDao;
import com.poo.petshop.dao.TosadorDao;
import com.poo.petshop.dao.VeterinarioDao;

import com.poo.petshop.model.Funcionario;
import com.poo.petshop.model.Veterinario;
import com.poo.petshop.model.Atendente;
import com.poo.petshop.model.Tosador;

import com.poo.petshop.service.AtendenteService;
import com.poo.petshop.service.TosadorService;
import com.poo.petshop.service.VeterinarioService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionarioController {

    private final VeterinarioService veterinarioService = new VeterinarioService(new VeterinarioDao());
    private final AtendenteService atendenteService = new AtendenteService(new AtendenteDao());
    private final TosadorService tosadorService = new TosadorService(new TosadorDao());

    @FXML
    private ComboBox<String> tipoFuncionarioComboBox;

    @FXML
    private TextField nomeField;
    @FXML
    private TextField cpfField;

    @FXML
    private DatePicker dataContratacaoPicker;

    @FXML
    private TextField matriculaField;
    @FXML
    private TextField pesquisaNomeFuncionarioField;

    @FXML
    private VBox veterinarioFields;
    @FXML
    private VBox atendenteFields;
    @FXML
    private VBox tosadorFields;

    @FXML
    private TextField crmvField;
    @FXML
    private TextField setorField;
    @FXML
    private TextField especialidadeField;

    @FXML
    private TableView<Funcionario> funcionarioTable;

    @FXML
    private TableColumn<Funcionario, String> nomeColumn;
    @FXML
    private TableColumn<Funcionario, String> cpfColumn;
    @FXML
    private TableColumn<Funcionario, String> matriculaColumn;
    @FXML
    private TableColumn<Funcionario, LocalDate> dataAdmissaoColumn;

    @FXML
    private TableColumn<Funcionario, Void> editarColumn;
    @FXML
    private TableColumn<Funcionario, Void> excluirColumn;

    @FXML
    private Label statusLabel;

    private final ObservableList<Funcionario> funcionarioList = FXCollections.observableArrayList();
    private Funcionario funcionarioSelecionadoParaEdicao;

    @FXML
    public void initialize() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        dataAdmissaoColumn.setCellValueFactory(new PropertyValueFactory<>("dataAdmissao"));

        funcionarioTable.setItems(funcionarioList);

        ObservableList<String> tipos = FXCollections.observableArrayList(
                "Veterinário", "Atendente", "Tosador"
        );
        tipoFuncionarioComboBox.setItems(tipos);
        tipoFuncionarioComboBox.setValue("Veterinário");

        tipoFuncionarioComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            mostrarCamposEspecificos(newValue);
        });

        editarColumn.setCellFactory(param -> new TableCell<Funcionario, Void>() {
            private final Button editButton = new Button("Editar");
            {
                editButton.setOnAction(event -> {
                    Funcionario funcionario = getTableView().getItems().get(getIndex());
                    preencherCamposParaEdicao(funcionario);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        excluirColumn.setCellFactory(param -> new TableCell<Funcionario, Void>() {
            private final Button deleteButton = new Button("Excluir");
            {
                deleteButton.setOnAction(event -> {
                    Funcionario funcionario = getTableView().getItems().get(getIndex());
                    confirmarExclusao(funcionario);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        carregarFuncionarios();
        mostrarCamposEspecificos("Veterinário");
    }

    private void mostrarCamposEspecificos(String tipoSelecionado) {
        veterinarioFields.setVisible(false);
        veterinarioFields.setManaged(false);
        atendenteFields.setVisible(false);
        atendenteFields.setManaged(false);
        tosadorFields.setVisible(false);
        tosadorFields.setManaged(false);

        switch (tipoSelecionado) {
            case "Veterinário":
                veterinarioFields.setVisible(true);
                veterinarioFields.setManaged(true);
                break;
            case "Atendente":
                atendenteFields.setVisible(true);
                atendenteFields.setManaged(true);
                break;
            case "Tosador":
                tosadorFields.setVisible(true);
                tosadorFields.setManaged(true);
                break;
            default:
                break;
        }
    }

    private void carregarFuncionarios() {
        try {
            String termoPesquisa = pesquisaNomeFuncionarioField.getText().trim();
            List<Funcionario> todosFuncionarios = new ArrayList<>();

            if (termoPesquisa.isEmpty()) {
                todosFuncionarios.addAll(veterinarioService.findAll());
                todosFuncionarios.addAll(atendenteService.findAll());
                todosFuncionarios.addAll(tosadorService.findAll());
                statusLabel.setText("Todos os funcionários carregados.");
            } else {
                try {
                    todosFuncionarios.addAll(veterinarioService.findByName(termoPesquisa));
                } catch (Exception ignore) { }
                try {
                    todosFuncionarios.addAll(atendenteService.findByName(termoPesquisa));
                } catch (Exception ignore) { }
                try {
                    todosFuncionarios.addAll(tosadorService.findByName(termoPesquisa));
                } catch (Exception ignore) { }

                if (todosFuncionarios.isEmpty()) {
                    statusLabel.setText("Nenhum funcionário encontrado com o nome '" + termoPesquisa + "'.");
                } else {
                    statusLabel.setText(todosFuncionarios.size() + " funcionários encontrados para '" + termoPesquisa + "'.");
                }
            }
            funcionarioList.setAll(todosFuncionarios);
        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar funcionários: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void salvarFuncionario() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        LocalDate dataAdmissao = dataContratacaoPicker.getValue();
        String matricula = matriculaField.getText();

        if (nome.isEmpty() || cpf.isEmpty() || matricula.isEmpty() || dataAdmissao == null) {
            statusLabel.setText("Por favor, preencha todos os campos obrigatórios (Nome, CPF, Matrícula, Data Admissão).");
            return;
        }

        String tipoSelecionado = tipoFuncionarioComboBox.getValue();

        try {
            switch (tipoSelecionado) {
                case "Veterinário":
                    String crmv = crmvField.getText();
                    if (crmv.isEmpty()) {
                        statusLabel.setText("CRMV é obrigatório para Veterinário.");
                        return;
                    }
                    Veterinario veterinario = (funcionarioSelecionadoParaEdicao instanceof Veterinario) ?
                            (Veterinario) funcionarioSelecionadoParaEdicao : new Veterinario(nome, cpf);

                    preencherCamposComuns(veterinario, nome, cpf, dataAdmissao, matricula);
                    veterinario.setCrmv(crmv);

                    if (veterinario.getId() == null) {
                        veterinarioService.save(veterinario);
                    } else {
                        veterinarioService.update(veterinario);
                    }
                    statusLabel.setText("Veterinário salvo com sucesso!");
                    break;

                case "Atendente":
                    String setor = setorField.getText();
                    if (setor.isEmpty()) {
                        statusLabel.setText("Setor é obrigatório para Atendente.");
                        return;
                    }
                    Atendente atendente = (funcionarioSelecionadoParaEdicao instanceof Atendente) ?
                            (Atendente) funcionarioSelecionadoParaEdicao : new Atendente(nome, cpf);

                    preencherCamposComuns(atendente, nome, cpf, dataAdmissao, matricula);
                    atendente.setSetor(setor);

                    if (atendente.getId() == null) {
                        atendenteService.save(atendente);
                    } else {
                        atendenteService.update(atendente);
                    }
                    statusLabel.setText("Atendente salvo com sucesso!");
                    break;

                case "Tosador":
                    String especialidade = especialidadeField.getText();
                    if (especialidade.isEmpty()) {
                        statusLabel.setText("Especialidade é obrigatória para Tosador.");
                        return;
                    }
                    Tosador tosador = (funcionarioSelecionadoParaEdicao instanceof Tosador) ?
                            (Tosador) funcionarioSelecionadoParaEdicao : new Tosador(nome, cpf);

                    preencherCamposComuns(tosador, nome, cpf, dataAdmissao, matricula);
                    tosador.setEspecialidade(especialidade);

                    if (tosador.getId() == null) {
                        tosadorService.save(tosador);
                    } else {
                        tosadorService.update(tosador);
                    }
                    statusLabel.setText("Tosador salvo com sucesso!");
                    break;
                default:
                    statusLabel.setText("Tipo de funcionário inválido selecionado.");
                    return;
            }
            limparCampos();
            funcionarioSelecionadoParaEdicao = null;
            carregarFuncionarios();
        } catch (IllegalArgumentException e) {
            statusLabel.setText("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Erro ao salvar funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void preencherCamposComuns(Funcionario func, String nome, String cpf, LocalDate dataAdmissao, String matricula) {
        if (func.getId() == null) {
            func.setNome(nome);
            func.setCpf(cpf);
        } else {
            func.setNome(nome);
            func.setCpf(cpf);
        }
        func.setMatricula(matricula);
        func.setDataAdmissao(dataAdmissao);
    }

    private void preencherCamposParaEdicao(Funcionario funcionario) {
        this.funcionarioSelecionadoParaEdicao = funcionario;

        nomeField.setText(funcionario.getNome());
        cpfField.setText(funcionario.getCpf());
        matriculaField.setText(funcionario.getMatricula());
        dataContratacaoPicker.setValue(funcionario.getDataAdmissao());

        if (funcionario instanceof Veterinario) {
            tipoFuncionarioComboBox.setValue("Veterinário");
            crmvField.setText(((Veterinario) funcionario).getCrmv());
        } else if (funcionario instanceof Atendente) {
            tipoFuncionarioComboBox.setValue("Atendente");
            setorField.setText(((Atendente) funcionario).getSetor());
        } else if (funcionario instanceof Tosador) {
            tipoFuncionarioComboBox.setValue("Tosador");
            especialidadeField.setText(((Tosador) funcionario).getEspecialidade());
        }
        statusLabel.setText("Editando funcionário: " + funcionario.getNome());
    }

    private void confirmarExclusao(Funcionario funcionario) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Funcionário?");
        alert.setContentText("Tem certeza que deseja excluir o funcionário: " + funcionario.getNome() + " (Matrícula: " + funcionario.getMatricula() + ")?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            excluirFuncionario(funcionario);
        }
    }

    private void excluirFuncionario(Funcionario funcionario) {
        try {
            if (funcionario instanceof Veterinario) {
                veterinarioService.delete(funcionario.getId());
            } else if (funcionario instanceof Atendente) {
                atendenteService.delete(funcionario.getId());
            } else if (funcionario instanceof Tosador) {
                tosadorService.delete(funcionario.getId());
            } else {
                statusLabel.setText("Erro: Tipo de funcionário desconhecido para exclusão.");
                return;
            }
            carregarFuncionarios();
            statusLabel.setText("Funcionário excluído com sucesso.");
            limparCampos();
            funcionarioSelecionadoParaEdicao = null;
        } catch (Exception e) {
            statusLabel.setText("Erro ao excluir funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void pesquisarFuncionarios() {
        carregarFuncionarios();
    }

    @FXML
    private void limparPesquisa() {
        pesquisaNomeFuncionarioField.clear();
        carregarFuncionarios();
    }

    @FXML
    private void abrirCadastroFuncionario(ActionEvent event) {
        limparCampos();
        funcionarioSelecionadoParaEdicao = null;
        statusLabel.setText("Pronto para novo cadastro de funcionário.");
    }

    @FXML
    private void voltarParaMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/poo/petshop/view/main-view.fxml"));
            Parent mainRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(mainRoot);
        } catch (IOException e) {
            System.err.println("Erro ao voltar para o menu principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void limparCampos() {
        nomeField.clear();
        cpfField.clear();
        dataContratacaoPicker.setValue(null);
        matriculaField.clear();
        crmvField.clear();
        setorField.clear();
        especialidadeField.clear();
        statusLabel.setText("");
        tipoFuncionarioComboBox.setValue("Veterinário");
        this.funcionarioSelecionadoParaEdicao = null;
    }
}