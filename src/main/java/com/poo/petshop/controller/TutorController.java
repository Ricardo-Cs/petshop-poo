package com.poo.petshop.controller;

import com.poo.petshop.dao.TutorDao;
import com.poo.petshop.model.Tutor;
import com.poo.petshop.service.TutorService;
import com.poo.petshop.util.exceptions.TutorNaoEncontradoException;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TutorController {

    private final TutorService tutorService = new TutorService(new TutorDao());

    @FXML
    private TextField nomeField;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField pesquisaNomeField;

    @FXML
    private TableView<Tutor> tutorTable;

    @FXML
    private TableColumn<Tutor, String> nomeColumn;

    @FXML
    private TableColumn<Tutor, String> cpfColumn;

    @FXML
    private TableColumn<Tutor, Void> editarColumn;
    @FXML
    private TableColumn<Tutor, Void> excluirColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Button voltarButton;

    private final ObservableList<Tutor> tutorList = FXCollections.observableArrayList();

    private Tutor tutorSelecionadoParaEdicao;

    @FXML
    public void initialize() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tutorTable.setItems(tutorList);

        editarColumn.setCellFactory(param -> new TableCell<Tutor, Void>() {
            private final Button editButton = new Button("Editar");

            {
                editButton.setOnAction(event -> {
                    Tutor tutor = getTableView().getItems().get(getIndex());
                    editarTutor(tutor);
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

        excluirColumn.setCellFactory(param -> new TableCell<Tutor, Void>() {
            private final Button deleteButton = new Button("Excluir");

            {
                deleteButton.setOnAction(event -> {
                    Tutor tutor = getTableView().getItems().get(getIndex());
                    confirmarExclusao(tutor);
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

        carregarTutores();
    }

    private void carregarTutores() {
        try {
            String termoPesquisa = pesquisaNomeField.getText().trim();
            List<Tutor> tutores;
            if (termoPesquisa.isEmpty()) {
                tutores = tutorService.findAll();
                statusLabel.setText("Todos os tutores carregados.");
            } else {
                tutores = tutorService.findByName(termoPesquisa);
                statusLabel.setText(tutores.size() + " tutores encontrados para '" + termoPesquisa + "'.");
            }
            tutorList.setAll(tutores);
        } catch (TutorNaoEncontradoException e) {
            tutorList.clear();
            statusLabel.setText(e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar tutores: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void salvarTutor() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();

        if (nome.isEmpty() || cpf.isEmpty()) {
            statusLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        Tutor tutor;
        if (tutorSelecionadoParaEdicao == null) {
            tutor = new Tutor();
        } else {
            tutor = tutorSelecionadoParaEdicao;
        }

        tutor.setNome(nome);
        tutor.setCpf(cpf);

        try {
            tutorService.save(tutor);
            carregarTutores();
            limparCampos();
            tutorSelecionadoParaEdicao = null;
            statusLabel.setText("Tutor salvo com sucesso.");
        } catch (IllegalArgumentException e) {
            statusLabel.setText("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Erro ao salvar tutor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void pesquisarTutores() {
        carregarTutores();
    }

    @FXML
    private void limparPesquisa() {
        pesquisaNomeField.clear();
        carregarTutores();
        statusLabel.setText("Exibindo todos os tutores.");
    }

    private void editarTutor(Tutor tutor) {
        nomeField.setText(tutor.getNome());
        cpfField.setText(tutor.getCpf());
        tutorSelecionadoParaEdicao = tutor;
        statusLabel.setText("Editando tutor: " + tutor.getNome());
    }

    private void confirmarExclusao(Tutor tutor) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Tutor?");
        alert.setContentText("Tem certeza que deseja excluir o tutor: " + tutor.getNome() + " (CPF: " + tutor.getCpf() + ")?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            excluirTutor(tutor);
        }
    }

    private void excluirTutor(Tutor tutor) {
        try {
            tutorService.delete(tutor.getId());
            carregarTutores(); // Recarrega a tabela (considerando a pesquisa atual)
            statusLabel.setText("Tutor excluído com sucesso.");
            limparCampos();
            tutorSelecionadoParaEdicao = null;
        } catch (Exception e) {
            statusLabel.setText("Erro ao excluir tutor: " + e.getMessage());
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

    private void limparCampos() {
        nomeField.clear();
        cpfField.clear();
    }
}