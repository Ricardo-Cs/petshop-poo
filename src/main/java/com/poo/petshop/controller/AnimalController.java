package com.poo.petshop.controller;

import com.poo.petshop.dao.AnimalDao;
import com.poo.petshop.model.Animal;
import com.poo.petshop.service.AnimalService;
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

public class AnimalController {

    private final AnimalService animalService = new AnimalService(new AnimalDao());

    @FXML
    private TextField nomeAnimalField;

    @FXML
    private TextField especieField;

    @FXML
    private TextField racaField;

    @FXML
    private TextField pesquisaNomeAnimalField;

    @FXML
    private TableView<Animal> animalTable;

    @FXML
    private TableColumn<Animal, String> nomeAnimalColumn;

    @FXML
    private TableColumn<Animal, String> especieColumn;

    @FXML
    private TableColumn<Animal, String> racaColumn;

    @FXML
    private TableColumn<Animal, Void> editarColumn;
    @FXML
    private TableColumn<Animal, Void> excluirColumn;

    @FXML
    private Label statusAnimalLabel;

    private final ObservableList<Animal> animalList = FXCollections.observableArrayList();

    private Animal animalSelecionadoParaEdicao;

    @FXML
    public void initialize() {
        nomeAnimalColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        especieColumn.setCellValueFactory(new PropertyValueFactory<>("especie"));
        racaColumn.setCellValueFactory(new PropertyValueFactory<>("raca"));

        animalTable.setItems(animalList);

        editarColumn.setCellFactory(param -> new TableCell<Animal, Void>() {
            private final Button editButton = new Button("Editar");

            {
                editButton.setOnAction(event -> {
                    Animal animal = getTableView().getItems().get(getIndex());
                    editarAnimal(animal);
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

        excluirColumn.setCellFactory(param -> new TableCell<Animal, Void>() {
            private final Button deleteButton = new Button("Excluir");

            {
                deleteButton.setOnAction(event -> {
                    Animal animal = getTableView().getItems().get(getIndex());
                    confirmarExclusao(animal);
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

        carregarAnimais();
    }

    private void carregarAnimais() {
        try {
            String termoPesquisa = pesquisaNomeAnimalField.getText().trim();
            List<Animal> animais;

            if (termoPesquisa.isEmpty()) {
                animais = animalService.findAll();
                statusAnimalLabel.setText("Todos os animais carregados.");
            } else {
                // Usa o método findByName do serviço para buscar no banco
                animais = animalService.findByName(termoPesquisa);
                statusAnimalLabel.setText(animais.size() + " animais encontrados para '" + termoPesquisa + "'.");
            }
            animalList.setAll(animais);
        } catch (Exception e) {
            statusAnimalLabel.setText("Erro ao carregar animais: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void salvarAnimal() {
        String nome = nomeAnimalField.getText();
        String especie = especieField.getText();
        String raca = racaField.getText();

        if (nome.isEmpty() || especie.isEmpty() || raca.isEmpty()) {
            statusAnimalLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        Animal animal;
        if (animalSelecionadoParaEdicao == null) {
            animal = new Animal();
        } else {
            animal = animalSelecionadoParaEdicao;
        }

        animal.setNome(nome);
        animal.setEspecie(especie);
        animal.setRaca(raca);

        try {
            animalService.save(animal);
            carregarAnimais();
            limparCampos();
            animalSelecionadoParaEdicao = null;
            statusAnimalLabel.setText("Animal salvo com sucesso.");
        } catch (IllegalArgumentException e) {
            statusAnimalLabel.setText("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            statusAnimalLabel.setText("Erro ao salvar animal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void pesquisarAnimais() {
        carregarAnimais();
    }

    @FXML
    private void limparPesquisaAnimal() {
        pesquisaNomeAnimalField.clear();
        carregarAnimais();
        statusAnimalLabel.setText("Exibindo todos os animais.");
    }

    private void editarAnimal(Animal animal) {
        nomeAnimalField.setText(animal.getNome());
        especieField.setText(animal.getEspecie());
        racaField.setText(animal.getRaca());
        animalSelecionadoParaEdicao = animal;
        statusAnimalLabel.setText("Editando animal: " + animal.getNome());
    }

    private void confirmarExclusao(Animal animal) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Animal?");
        alert.setContentText("Tem certeza que deseja excluir o animal: " + animal.getNome() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            excluirAnimal(animal);
        }
    }

    private void excluirAnimal(Animal animal) {
        try {
            animalService.delete(animal.getId());
            carregarAnimais();
            statusAnimalLabel.setText("Animal excluído com sucesso.");
            limparCampos();
            animalSelecionadoParaEdicao = null;
        } catch (Exception e) {
            statusAnimalLabel.setText("Erro ao excluir animal: " + e.getMessage());
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
        nomeAnimalField.clear();
        especieField.clear();
        racaField.clear();
    }
}