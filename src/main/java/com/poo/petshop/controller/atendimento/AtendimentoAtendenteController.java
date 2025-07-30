package com.poo.petshop.controller.atendimento;

import com.poo.petshop.dao.AtendenteDao;
import com.poo.petshop.dao.AtendimentoAtendenteDao;
import com.poo.petshop.dao.TosadorDao;
import com.poo.petshop.dao.TutorDao;

import com.poo.petshop.model.Atendente;
import com.poo.petshop.model.AtendimentoAtendente;
import com.poo.petshop.model.Tosador;
import com.poo.petshop.model.Tutor;
import com.poo.petshop.model.enums.ETipoAtendimento;

import com.poo.petshop.service.AtendenteService;
import com.poo.petshop.service.AtendimentoAtendenteService;
import com.poo.petshop.service.TosadorService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AtendimentoAtendenteController {

    private final AtendenteService atendenteService = new AtendenteService(new AtendenteDao());
    private final TutorService tutorService = new TutorService(new TutorDao());
    private final TosadorService tosadorService = new TosadorService(new TosadorDao());
    private final AtendimentoAtendenteService atendimentoAtendenteService = new AtendimentoAtendenteService(new AtendimentoAtendenteDao());

    @FXML
    private ComboBox<Atendente> atendenteComboBox;
    @FXML
    private ComboBox<Tutor> tutorComboBox;
    @FXML
    private ComboBox<ETipoAtendimento> tipoAtendimentoComboBox;

    @FXML
    private Label tosadorLabel;
    @FXML
    private ComboBox<Tosador> tosadorComboBox;

    @FXML
    private TableView<AtendimentoAtendente> atendimentosTable;
    @FXML
    private TableColumn<AtendimentoAtendente, String> atendenteColumn;
    @FXML
    private TableColumn<AtendimentoAtendente, String> tutorColumn;
    @FXML
    private TableColumn<AtendimentoAtendente, ETipoAtendimento> tipoAtendimentoColumn;
    @FXML
    private TableColumn<AtendimentoAtendente, String> tosadorColumn;
    @FXML
    private TableColumn<AtendimentoAtendente, Void> editarColumn;
    @FXML
    private TableColumn<AtendimentoAtendente, Void> excluirColumn;

    @FXML
    private Label statusLabel;

    private final ObservableList<AtendimentoAtendente> atendimentoList = FXCollections.observableArrayList();
    private AtendimentoAtendente atendimentoSelecionadoParaEdicao;

    @FXML
    public void initialize() {
        atendenteComboBox.setItems(FXCollections.observableArrayList(atendenteService.findAll()));
        atendenteComboBox.setConverter(new StringConverter<Atendente>() {
            @Override
            public String toString(Atendente atendente) {
                return (atendente != null) ? atendente.getNome() : "";
            }
            @Override
            public Atendente fromString(String string) {
                return null;
            }
        });

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

        tipoAtendimentoComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(ETipoAtendimento.values())));
        tipoAtendimentoComboBox.setValue(ETipoAtendimento.COMPRA);

        tosadorComboBox.setItems(FXCollections.observableArrayList(tosadorService.findAll()));
        tosadorComboBox.setConverter(new StringConverter<Tosador>() {
            @Override
            public String toString(Tosador tosador) {
                return (tosador != null) ? tosador.getNome() : "";
            }
            @Override
            public Tosador fromString(String string) {
                return null;
            }
        });

        tipoAtendimentoComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            boolean showTosador = (newValue == ETipoAtendimento.BANHO || newValue == ETipoAtendimento.TOSA);
            tosadorLabel.setVisible(showTosador);
            tosadorLabel.setManaged(showTosador);
            tosadorComboBox.setVisible(showTosador);
            tosadorComboBox.setManaged(showTosador);

            if (!showTosador) {
                tosadorComboBox.getSelectionModel().clearSelection();
            }
        });

        // Garante a visibilidade inicial correta ao carregar
        boolean initialShowTosador = (tipoAtendimentoComboBox.getValue() == ETipoAtendimento.BANHO || tipoAtendimentoComboBox.getValue() == ETipoAtendimento.TOSA);
        tosadorLabel.setVisible(initialShowTosador);
        tosadorLabel.setManaged(initialShowTosador);
        tosadorComboBox.setVisible(initialShowTosador);
        tosadorComboBox.setManaged(initialShowTosador);


        atendenteColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAtendente().getNome()));
        tutorColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTutor().getNome()));
        tipoAtendimentoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoAtendimento"));

        tosadorColumn.setCellValueFactory(data -> {
            Tosador tosador = data.getValue().getTosador();
            return new javafx.beans.property.SimpleStringProperty(tosador != null ? tosador.getNome() : "-");
        });


        atendimentosTable.setItems(atendimentoList);

        editarColumn.setCellFactory(param -> new TableCell<AtendimentoAtendente, Void>() {
            private final Button editButton = new Button("Editar");
            {
                editButton.setOnAction(event -> {
                    AtendimentoAtendente atendimento = getTableView().getItems().get(getIndex());
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

        excluirColumn.setCellFactory(param -> new TableCell<AtendimentoAtendente, Void>() {
            private final Button deleteButton = new Button("Excluir");
            {
                deleteButton.setOnAction(event -> {
                    AtendimentoAtendente atendimento = getTableView().getItems().get(getIndex());
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

        carregarAtendimentosAtendente();
    }

    private void carregarAtendimentosAtendente() {
        try {
            List<AtendimentoAtendente> atendimentos = atendimentoAtendenteService.findAll();
            atendimentoList.setAll(atendimentos);
            statusLabel.setText("Atendimentos carregados.");
        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar atendimentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void salvarAtendimentoAtendente() {
        Atendente atendente = atendenteComboBox.getValue();
        Tutor tutor = tutorComboBox.getValue();
        ETipoAtendimento tipoAtendimento = tipoAtendimentoComboBox.getValue();
        Tosador tosador = tosadorComboBox.getValue();

        if (atendente == null || tutor == null || tipoAtendimento == null) {
            statusLabel.setText("Por favor, selecione um atendente, um tutor e um tipo de atendimento.");
            return;
        }

        if ((tipoAtendimento == ETipoAtendimento.BANHO || tipoAtendimento == ETipoAtendimento.TOSA) && tosador == null) {
            statusLabel.setText("Para Banho ou Tosa, selecione o tosador.");
            return;
        }

        AtendimentoAtendente atendimento;
        if (atendimentoSelecionadoParaEdicao == null) {
            atendimento = new AtendimentoAtendente();
        } else {
            atendimento = atendimentoSelecionadoParaEdicao;
        }

        atendimento.setAtendente(atendente);
        atendimento.setTutor(tutor);
        atendimento.setTipoAtendimento(tipoAtendimento);
        atendimento.setTosador(tosador);

        try {
            if (atendimento.getId() == null) {
                atendimentoAtendenteService.save(atendimento);
            } else {
                atendimentoAtendenteService.update(atendimento);
            }
            statusLabel.setText("Atendimento salvo com sucesso!");
            limparCampos();
            atendimentoSelecionadoParaEdicao = null;
            carregarAtendimentosAtendente();
        } catch (Exception e) {
            statusLabel.setText("Erro ao salvar atendimento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void preencherCamposParaEdicao(AtendimentoAtendente atendimento) {
        this.atendimentoSelecionadoParaEdicao = atendimento;

        atendenteComboBox.setValue(atendimento.getAtendente());
        tutorComboBox.setValue(atendimento.getTutor());
        tipoAtendimentoComboBox.setValue(atendimento.getTipoAtendimento());

        if (atendimento.getTipoAtendimento() == ETipoAtendimento.BANHO || atendimento.getTipoAtendimento() == ETipoAtendimento.TOSA) {
            tosadorLabel.setVisible(true);
            tosadorLabel.setManaged(true);
            tosadorComboBox.setVisible(true);
            tosadorComboBox.setManaged(true);
            tosadorComboBox.setValue(atendimento.getTosador());
        } else {
            tosadorLabel.setVisible(false);
            tosadorLabel.setManaged(false);
            tosadorComboBox.setVisible(false);
            tosadorComboBox.setManaged(false);
            tosadorComboBox.getSelectionModel().clearSelection();
        }

        statusLabel.setText("Editando atendimento para " + atendimento.getTutor().getNome() + " por " + atendimento.getAtendente().getNome());
    }

    private void confirmarExclusao(AtendimentoAtendente atendimento) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Atendimento?");
        alert.setContentText("Tem certeza que deseja excluir o atendimento do tipo '" + atendimento.getTipoAtendimento() + "' para o tutor " + atendimento.getTutor().getNome() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            excluirAtendimentoAtendente(atendimento);
        }
    }

    private void excluirAtendimentoAtendente(AtendimentoAtendente atendimento) {
        try {
            atendimentoAtendenteService.delete(atendimento.getId());
            statusLabel.setText("Atendimento excluído com sucesso!");
            limparCampos();
            atendimentoSelecionadoParaEdicao = null;
            carregarAtendimentosAtendente();
        } catch (Exception e) {
            statusLabel.setText("Erro ao excluir atendimento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void limparCampos() {
        atendenteComboBox.getSelectionModel().clearSelection();
        tutorComboBox.getSelectionModel().clearSelection();
        tipoAtendimentoComboBox.getSelectionModel().clearSelection();
        tipoAtendimentoComboBox.setValue(ETipoAtendimento.COMPRA);

        tosadorComboBox.getSelectionModel().clearSelection();
        tosadorLabel.setVisible(false);
        tosadorLabel.setManaged(false);
        tosadorComboBox.setVisible(false);
        tosadorComboBox.setManaged(false);

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
