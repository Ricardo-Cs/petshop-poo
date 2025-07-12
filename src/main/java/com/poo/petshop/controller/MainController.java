package com.poo.petshop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
// Importações do logger foram removidas

public class MainController {

    // A instância do logger foi removida

    @FXML
    private Label myLabel;

    @FXML
    public void initialize() {
        System.out.println("MainController inicializado. Label: " + myLabel.getText()); // Usando System.out
    }

    @FXML
    private void handleButtonClick(ActionEvent event) {
        myLabel.setText("Texto alterado pelo botão!");
        System.out.println("Botão clicado. Texto do label alterado."); // Usando System.out
    }
}