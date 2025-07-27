package com.poo.petshop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App extends Application {

    private static EntityManagerFactory entityManagerFactory;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            System.out.println("Iniciando Hibernate...");
            entityManagerFactory = Persistence.createEntityManagerFactory("petshop-pu");
            System.out.println("Hibernate iniciado com sucesso.");

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            stage.setTitle("PET SHOP");
            stage.setScene(scene);
            stage.show();
            System.out.println("Aplicação JavaFX iniciada com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar a aplicação ou Hibernate: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            System.out.println("EntityManagerFactory do Hibernate encerrado.");
        }
        System.out.println("Aplicação JavaFX encerrada.");
    }

    public static void main(String[] args) {
        launch();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}