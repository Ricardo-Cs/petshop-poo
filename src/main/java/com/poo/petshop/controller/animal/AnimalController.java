package com.poo.petshop.controller.animal;

import com.poo.petshop.dao.AnimalDao;
import com.poo.petshop.dao.TutorDao;
import com.poo.petshop.model.Animal;
import com.poo.petshop.model.Tutor;
import com.poo.petshop.service.AnimalService;
import com.poo.petshop.service.TutorService;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AnimalController {

    private final AnimalService animalService = new AnimalService(new AnimalDao());
    private final TutorService tutorService = new TutorService(new TutorDao());

    @FXML
    private TextField nomeAnimalField;

    @FXML
    private TextField especieField;

    @FXML
    private TextField racaField;

    @FXML
    private ComboBox<Tutor> tutorComboBox;

    @FXML
    private TextField pesquisaNomeAnimalField;

    @FXML
    private TableView<Animal> animalTable;

    @FXML
    private TableColumn<Animal, Long> idColumn;

    @FXML
    private TableColumn<Animal, String> nomeAnimalColumn;

    @FXML
    private TableColumn<Animal, String> especieColumn;

    @FXML
    private TableColumn<Animal, String> racaColumn;

    @FXML
    private TableColumn<Animal, String> tutorColumn;

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
        tutorComboBox.setItems(FXCollections.observableArrayList(tutorService.findAll()));
        tutorComboBox.setConverter(new StringConverter<Tutor>() {
            @Override
            public String toString(Tutor tutor) {
                return (tutor != null) ? tutor.getNome() : "";
            }
            @Override
            public Tutor fromString(String string) {
                return null;
            }
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeAnimalColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        especieColumn.setCellValueFactory(new PropertyValueFactory<>("especie"));
        racaColumn.setCellValueFactory(new PropertyValueFactory<>("raca"));

        tutorColumn.setCellValueFactory(data -> {
            Animal animal = data.getValue();
            if (animal != null && animal.getTutor() != null) {
                return new javafx.beans.property.SimpleStringProperty(animal.getTutor().getNome());
            }
            return new javafx.beans.property.SimpleStringProperty("Sem Tutor");
        });

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
        Tutor tutor = tutorComboBox.getValue();

        if (nome.isEmpty() || especie.isEmpty() || raca.isEmpty() || tutor == null) {
            statusAnimalLabel.setText("Por favor, preencha todos os campos e selecione um tutor.");
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
        animal.setTutor(tutor);

        try {
            if (animal.getId() == null) {
                animalService.save(animal);
            } else {
                animalService.update(animal);
            }
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
        tutorComboBox.setValue(animal.getTutor());
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
        tutorComboBox.getSelectionModel().clearSelection();
    }
}