package com.poo.petshop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("PET SHOP");
            stage.setScene(scene);
            stage.show();
            System.out.println("Aplicação JavaFX iniciada com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML. Verifique o caminho e a sintaxe: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        System.out.println("Aplicação JavaFX encerrada.");
    }

    public static void main(String[] args) {
        launch();
    }
}