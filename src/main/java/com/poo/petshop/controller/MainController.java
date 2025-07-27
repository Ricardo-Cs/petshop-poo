package com.poo.petshop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private VBox root;

    @FXML
    public void initialize() {
        System.out.println("MainController inicializado.");
    }

    @FXML
    private void abrirTelaTutor(ActionEvent event) {
        carregarTela("/com/poo/petshop/view/tutor-view.fxml");
    }

    @FXML
    private void abrirTelaAnimal(ActionEvent event) {
        carregarTela("/com/poo/petshop/view/animal-view.fxml");
    }

    @FXML
    private void abrirAtendimentoAtendente(ActionEvent event) {
        carregarTela("/com/poo/petshop/view/atendimento-atendente-view.fxml");
    }

    @FXML
    private void abrirAtendimentoVeterinario(ActionEvent event) {
        carregarTela("/com/poo/petshop/view/atendimento-veterinario-view.fxml");
    }

    @FXML
    private void sairAplicacao(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private void carregarTela(String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent novaRoot = loader.load();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.getScene().setRoot(novaRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
