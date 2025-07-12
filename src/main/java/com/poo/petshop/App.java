package com.poo.petshop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Importações do logger foram removidas

public class App extends Application {

    // A instância do logger foi removida

    // As variáveis do EntityManagerFactory e EntityManager não são necessárias para o teste de UI puro
    // mas se você for integrar o banco de dados depois, elas voltarão.
    // private static EntityManagerFactory entityManagerFactory;
    // private static EntityManager entityManager;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/MainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400); // Tamanho da janela
            stage.setTitle("Teste Simples JavaFX"); // Título da janela
            stage.setScene(scene);
            stage.show();
            System.out.println("Aplicação JavaFX iniciada com sucesso."); // Usando System.out
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML. Verifique o caminho e a sintaxe: " + e.getMessage()); // Usando System.err para erros
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        // Nada de logger ou banco de dados para fechar aqui
        System.out.println("Aplicação JavaFX encerrada."); // Usando System.out
    }

    // O método getEntityManager() também foi removido, pois não é mais necessário sem a integração DB aqui.
    // public static EntityManager getEntityManager() {
    //     return entityManager;
    // }

    public static void main(String[] args) {
        launch();
    }
}