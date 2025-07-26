package com.poo.petshop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML
    private Label myLabel;

    @FXML
    private VBox root; // id do container raiz do main.fxml

    @FXML
    public void initialize() {
        System.out.println("MainController inicializado. Label: " + myLabel.getText());
    }

    @FXML
    private void handleButtonClick(ActionEvent event) {
        myLabel.setText("Texto alterado pelo botão!");
        System.out.println("Botão clicado. Texto do label alterado.");
    }

    @FXML
    private void abrirTelaTutor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/poo/petshop/view/tutor-view.fxml"));
            Parent tutorRoot = loader.load();

            // Pega o stage atual via qualquer nó (botão ou root)
            Stage stage = (Stage) root.getScene().getWindow();

            // Troca o root da cena atual pelo da tela de tutor
            stage.getScene().setRoot(tutorRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
