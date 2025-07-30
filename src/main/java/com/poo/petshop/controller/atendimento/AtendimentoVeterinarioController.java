package com.poo.petshop.controller.atendimento;

import com.poo.petshop.dao.AnimalDao;
import com.poo.petshop.dao.AtendimentoVeterinarioDao;
import com.poo.petshop.dao.VeterinarioDao;

import com.poo.petshop.model.Animal;
import com.poo.petshop.model.AtendimentoVeterinario;
import com.poo.petshop.model.Veterinario;
import com.poo.petshop.model.enums.EStatusAtendimento;

import com.poo.petshop.service.AnimalService;
import com.poo.petshop.service.AtendimentoVeterinarioService;
import com.poo.petshop.service.VeterinarioService;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AtendimentoVeterinarioController {

    private final VeterinarioService veterinarioService = new VeterinarioService(new VeterinarioDao());
    private final AnimalService animalService = new AnimalService(new AnimalDao());
    private final AtendimentoVeterinarioService atendimentoVeterinarioService = new AtendimentoVeterinarioService(new AtendimentoVeterinarioDao());

    @FXML
    private ComboBox<Veterinario> veterinarioComboBox;
    @FXML
    private ComboBox<Animal> animalComboBox;
    @FXML
    private DatePicker dataEntradaPicker;
    @FXML
    private TextField horaEntradaField;
    @FXML
    private DatePicker dataSaidaPicker;
    @FXML
    private TextField horaSaidaField;
    @FXML
    private ComboBox<EStatusAtendimento> statusComboBox;

    @FXML
    private TableView<AtendimentoVeterinario> atendimentosTable;
    @FXML
    private TableColumn<AtendimentoVeterinario, String> veterinarioColumn;
    @FXML
    private TableColumn<AtendimentoVeterinario, String> animalColumn;
    @FXML
    private TableColumn<AtendimentoVeterinario, LocalDateTime> entradaColumn;
    @FXML
    private TableColumn<AtendimentoVeterinario, LocalDateTime> saidaColumn;
    @FXML
    private TableColumn<AtendimentoVeterinario, EStatusAtendimento> statusColumn;
    @FXML
    private TableColumn<AtendimentoVeterinario, Void> editarColumn;
    @FXML
    private TableColumn<AtendimentoVeterinario, Void> excluirColumn;

    @FXML
    private Label statusLabel;

    private final ObservableList<AtendimentoVeterinario> atendimentoList = FXCollections.observableArrayList();

    private AtendimentoVeterinario atendimentoSelecionadoParaEdicao;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


    @FXML
    public void initialize() {
        veterinarioComboBox.setItems(FXCollections.observableArrayList(veterinarioService.findAll()));
        veterinarioComboBox.setConverter(new StringConverter<Veterinario>() {
            @Override
            public String toString(Veterinario veterinario) {
                return (veterinario != null) ? veterinario.getNome() : "";
            }
            @Override
            public Veterinario fromString(String string) {
                return null;
            }
        });

        animalComboBox.setItems(FXCollections.observableArrayList(animalService.findAll()));
        animalComboBox.setConverter(new StringConverter<Animal>() {
            @Override
            public String toString(Animal animal) {
                return (animal != null) ? animal.getNome() + " (" + animal.getTutor().getNome() + ")" : "";
            }
            @Override
            public Animal fromString(String string) {
                return null;
            }
        });

        statusComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(EStatusAtendimento.values())));
        statusComboBox.setValue(EStatusAtendimento.ESPERA);

        dataEntradaPicker.setValue(LocalDate.now());
        horaEntradaField.setText(LocalTime.now().format(timeFormatter));
        dataSaidaPicker.setValue(null);
        horaSaidaField.setText("");

        veterinarioColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getVeterinario().getNome()));
        animalColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAnimal().getNome()));

        entradaColumn.setCellValueFactory(new PropertyValueFactory<>("entrada"));
        entradaColumn.setCellFactory(column -> new TableCell<AtendimentoVeterinario, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });

        saidaColumn.setCellValueFactory(new PropertyValueFactory<>("saida"));
        saidaColumn.setCellFactory(column -> new TableCell<AtendimentoVeterinario, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        atendimentosTable.setItems(atendimentoList);

        editarColumn.setCellFactory(param -> new TableCell<AtendimentoVeterinario, Void>() {
            private final Button editButton = new Button("Editar");
            {
                editButton.setOnAction(event -> {
                    AtendimentoVeterinario atendimento = getTableView().getItems().get(getIndex());
                    preencherCamposParaEdicao(atendimento);
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

        excluirColumn.setCellFactory(param -> new TableCell<AtendimentoVeterinario, Void>() {
            private final Button deleteButton = new Button("Excluir");
            {
                deleteButton.setOnAction(event -> {
                    AtendimentoVeterinario atendimento = getTableView().getItems().get(getIndex());
                    confirmarExclusao(atendimento);
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

        carregarAtendimentosVeterinario();
    }

    private void carregarAtendimentosVeterinario() {
        try {
            List<AtendimentoVeterinario> atendimentos = atendimentoVeterinarioService.findAll();
            atendimentoList.setAll(atendimentos);
            statusLabel.setText("Atendimentos carregados.");
        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar atendimentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void salvarAtendimentoVeterinario() {
        Veterinario veterinario = veterinarioComboBox.getValue();
        Animal animal = animalComboBox.getValue();
        LocalDate dataEntrada = dataEntradaPicker.getValue();
        String horaEntradaText = horaEntradaField.getText();
        LocalDate dataSaida = dataSaidaPicker.getValue();
        String horaSaidaText = horaSaidaField.getText();
        EStatusAtendimento status = statusComboBox.getValue();

        if (veterinario == null || animal == null || dataEntrada == null || horaEntradaText.isEmpty() || status == null) {
            statusLabel.setText("Por favor, preencha os campos obrigatórios: Veterinário, Animal, Data/Hora Entrada, Status.");
            return;
        }

        LocalDateTime entrada;
        try {
            LocalTime horaEntrada = LocalTime.parse(horaEntradaText, timeFormatter);
            entrada = LocalDateTime.of(dataEntrada, horaEntrada);
        } catch (DateTimeParseException e) {
            statusLabel.setText("Formato de hora de entrada inválido. Use HH:mm.");
            return;
        }

        LocalDateTime saida = null;
        if (dataSaida != null && !horaSaidaText.isEmpty()) {
            try {
                LocalTime horaSaida = LocalTime.parse(horaSaidaText, timeFormatter);
                saida = LocalDateTime.of(dataSaida, horaSaida);
            } catch (DateTimeParseException e) {
                statusLabel.setText("Formato de hora de saída inválido. Use HH:mm.");
                return;
            }
        }

        AtendimentoVeterinario atendimento;
        if (atendimentoSelecionadoParaEdicao == null) {
            atendimento = new AtendimentoVeterinario();
        } else {
            atendimento = atendimentoSelecionadoParaEdicao;
        }

        atendimento.setVeterinario(veterinario);
        atendimento.setAnimal(animal);
        atendimento.setEntrada(entrada);
        atendimento.setSaida(saida);
        atendimento.setStatus(status);

        try {
            if (atendimento.getId() == null) {
                atendimentoVeterinarioService.save(atendimento);
            } else {
                atendimentoVeterinarioService.update(atendimento);
            }
            statusLabel.setText("Atendimento salvo com sucesso!");
            limparCampos();
            atendimentoSelecionadoParaEdicao = null;
            carregarAtendimentosVeterinario();
        } catch (IllegalArgumentException e) {
            statusLabel.setText("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Erro ao salvar atendimento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void preencherCamposParaEdicao(AtendimentoVeterinario atendimento) {
        this.atendimentoSelecionadoParaEdicao = atendimento;

        veterinarioComboBox.setValue(atendimento.getVeterinario());
        animalComboBox.setValue(atendimento.getAnimal());
        dataEntradaPicker.setValue(atendimento.getEntrada().toLocalDate());
        horaEntradaField.setText(atendimento.getEntrada().format(timeFormatter));

        if (atendimento.getSaida() != null) {
            dataSaidaPicker.setValue(atendimento.getSaida().toLocalDate());
            horaSaidaField.setText(atendimento.getSaida().format(timeFormatter));
        } else {
            dataSaidaPicker.setValue(null);
            horaSaidaField.clear();
        }
        statusComboBox.setValue(atendimento.getStatus());

        statusLabel.setText("Editando atendimento para " + atendimento.getAnimal().getNome());
    }

    private void confirmarExclusao(AtendimentoVeterinario atendimento) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Atendimento Veterinário?");
        alert.setContentText("Tem certeza que deseja excluir o atendimento do animal " + atendimento.getAnimal().getNome() + " em " + atendimento.getEntrada().toLocalDate() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            excluirAtendimentoVeterinario(atendimento);
        }
    }

    private void excluirAtendimentoVeterinario(AtendimentoVeterinario atendimento) {
        try {
            atendimentoVeterinarioService.delete(atendimento.getId());
            statusLabel.setText("Atendimento excluído com sucesso!");
            limparCampos();
            atendimentoSelecionadoParaEdicao = null;
            carregarAtendimentosVeterinario();
        } catch (Exception e) {
            statusLabel.setText("Erro ao excluir atendimento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void limparCampos() {
        veterinarioComboBox.getSelectionModel().clearSelection();
        animalComboBox.getSelectionModel().clearSelection();
        dataEntradaPicker.setValue(LocalDate.now());
        horaEntradaField.setText(LocalTime.now().format(timeFormatter));
        dataSaidaPicker.setValue(null);
        horaSaidaField.clear();
        statusComboBox.setValue(EStatusAtendimento.ESPERA);
        statusLabel.setText("");
        atendimentoSelecionadoParaEdicao = null;
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
}
