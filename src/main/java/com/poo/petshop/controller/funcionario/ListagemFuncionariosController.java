package com.poo.petshop.controller.funcionario;

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

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListagemFuncionariosController {

    private final VeterinarioService veterinarioService = new VeterinarioService(new VeterinarioDao());
    private final AtendenteService atendenteService = new AtendenteService(new AtendenteDao());
    private final TosadorService tosadorService = new TosadorService(new TosadorDao());

    @FXML
    private TextField pesquisaNomeFuncionarioField;

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
    private TableColumn<Funcionario, String> tipoColumn;

    @FXML
    private TableColumn<Funcionario, Void> editarColumn;
    @FXML
    private TableColumn<Funcionario, Void> excluirColumn;

    @FXML
    private Label statusLabel;

    private final ObservableList<Funcionario> funcionarioList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        dataAdmissaoColumn.setCellValueFactory(new PropertyValueFactory<>("dataAdmissao"));

        // Para pegar o Tipo do Funcionário
        tipoColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Funcionario, String>, ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Funcionario, String> param) {
                Funcionario funcionario = param.getValue();
                String tipo = "Desconhecido";
                if (funcionario instanceof Veterinario) {
                    tipo = "Veterinário";
                } else if (funcionario instanceof Atendente) {
                    tipo = "Atendente";
                } else if (funcionario instanceof Tosador) {
                    tipo = "Tosador";
                }
                return new javafx.beans.property.SimpleStringProperty(tipo);
            }
        });

        funcionarioTable.setItems(funcionarioList);

        editarColumn.setCellFactory(param -> new TableCell<Funcionario, Void>() {
            private final Button editButton = new Button("Editar");
            {
                editButton.setOnAction(event -> {
                    Funcionario funcionario = getTableView().getItems().get(getIndex());
                    abrirTelaEdicaoFuncionario(event, funcionario);
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
                } catch (Exception ignore) { /* Ignora se não encontrar em Veterinário */ }
                try {
                    todosFuncionarios.addAll(atendenteService.findByName(termoPesquisa));
                } catch (Exception ignore) { /* Ignora se não encontrar em Atendente */ }
                try {
                    todosFuncionarios.addAll(tosadorService.findByName(termoPesquisa));
                } catch (Exception ignore) { /* Ignora se não encontrar em Tosador */ }

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
    private void pesquisarFuncionarios() {
        carregarFuncionarios();
    }

    @FXML
    private void limparPesquisa() {
        pesquisaNomeFuncionarioField.clear();
        carregarFuncionarios();
    }

    @FXML
    private void abrirTelaCadastroFuncionario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/poo/petshop/view/cadastro-funcionario-view.fxml"));
            Parent cadastroFuncionarioRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(cadastroFuncionarioRoot);
        } catch (IOException e) {
            System.err.println("Erro ao abrir tela de cadastro de funcionários: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de cadastro.");
            alert.setContentText("Verifique o caminho do arquivo FXML: /com/poo/petshop/view/cadastro-funcionario-view.fxml");
            alert.showAndWait();
        }
    }

    private void abrirTelaEdicaoFuncionario(ActionEvent event, Funcionario funcionarioParaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/poo/petshop/view/cadastro-funcionario-view.fxml"));
            Parent cadastroFuncionarioRoot = loader.load();

            CadastroFuncionarioController cadastroController = loader.getController();
            cadastroController.setFuncionarioParaEdicao(funcionarioParaEditar); // Passa o funcionário para o controlador de cadastro

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(cadastroFuncionarioRoot);
        } catch (IOException e) {
            System.err.println("Erro ao abrir tela de edição de funcionários: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de edição.");
            alert.setContentText("Verifique o caminho do arquivo FXML ou o método setFuncionarioParaEdicao.");
            alert.showAndWait();
        }
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
        } catch (Exception e) {
            statusLabel.setText("Erro ao excluir funcionário: " + e.getMessage());
            e.printStackTrace();
        }
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
}