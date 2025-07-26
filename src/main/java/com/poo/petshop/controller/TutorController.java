package com.poo.petshop.controller;

import com.poo.petshop.dao.TutorDao;
import com.poo.petshop.model.Tutor;
import com.poo.petshop.service.TutorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class TutorController {

    private final TutorService tutorService = new TutorService(new TutorDao());

    @FXML
    private TextField nomeField;

    @FXML
    private TextField cpfField;

    @FXML
    private TableView<Tutor> tutorTable;

    @FXML
    private TableColumn<Tutor, String> nomeColumn;

    @FXML
    private TableColumn<Tutor, String> cpfColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Button voltarButton;

    private final ObservableList<Tutor> tutorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nomeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        cpfColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCpf()));

        tutorTable.setItems(tutorList);
        carregarTutores();
    }

    private void carregarTutores() {
        try {
            tutorList.setAll(tutorService.findAll());
            statusLabel.setText("Tutores carregados.");
        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar tutores.");
            e.printStackTrace();
        }
    }

    @FXML
    private void salvarTutor() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();

        Tutor tutor = new Tutor();
        tutor.setNome(nome);
        tutor.setCpf(cpf);

        try {
            tutorService.save(tutor);
            carregarTutores();
            limparCampos();
            statusLabel.setText("Tutor salvo com sucesso.");
        } catch (Exception e) {
            statusLabel.setText("Erro: " + e.getMessage());
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
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        nomeField.clear();
        cpfField.clear();
    }

}
