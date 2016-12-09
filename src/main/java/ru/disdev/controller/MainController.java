package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.fontawesome.Icon;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.MainApplication;
import ru.disdev.entity.Column;
import ru.disdev.entity.Crud;
import ru.disdev.entity.crud.Answer;
import ru.disdev.entity.crud.Poll;
import ru.disdev.entity.crud.Question;
import ru.disdev.service.AnswerService;
import ru.disdev.service.PollService;
import ru.disdev.service.QuestionService;
import ru.disdev.tasks.ExportResultService;
import ru.disdev.utils.PopupUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class MainController implements Controller {

    private static final FileChooser.ExtensionFilter CSV_FILER = new FileChooser.ExtensionFilter("CSV file", "*.csv");
    @FXML
    private TabPane tabs;
    @FXML
    private JFXButton editButton;
    @FXML
    private TableView<Poll> pollTable;
    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableView<Answer> answerTable;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private BorderPane root;
    @FXML
    private JFXButton newResultButton;
    @FXML
    private JFXButton exportButton;
    @FXML
    private JFXSpinner spinner;

    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private final FileChooser fileChooser = new FileChooser();

    private int selectedItem;
    private int selectedCrud;

    @Override
    public void initialize() {
        newResultButton.setOnAction(this::onNewResultButtonClick);
        newResultButton.setGraphic(new Icon("PLUS_CIRCLE"));
        newResultButton.setText("");
        exportButton.setOnAction(this::onExportButtonClick);
        exportButton.setGraphic(new Icon("DOWNLOAD"));
        exportButton.setText("");
        editButton.setText("");
        editButton.setOnAction(this::onEditButtonClick);
        editButton.setGraphic(new Icon("PENCIL"));
        editButton.setVisible(false);
        directoryChooser.setTitle("Директория для экспорта");
        fileChooser.setTitle("Файл для импорта");
        fileChooser.setSelectedExtensionFilter(CSV_FILER);
        deleteButton.setVisible(false);
        Icon trash = new Icon("TRASH");
        trash.setTextFill(Color.RED);
        deleteButton.setGraphic(trash);
        deleteButton.setOnAction(this::onDeleteButtonClick);
        tabs.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> selectedCrud = newValue.intValue());
        tabs.getSelectionModel().select(0);
        Stream.of(pollTable, questionTable, answerTable).forEach(tableView ->
                tableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                    selectedItem = newValue.intValue();
                    deleteButton.setVisible(selectedItem != -1);
                    editButton.setVisible(selectedItem != -1);
                }));
        fillTableColums(Poll.class, pollTable);
        fillTableColums(Question.class, questionTable);
        fillTableColums(Answer.class, answerTable);
        pollTable.setItems(PollService.getInstance().getPolls());
        questionTable.setItems(QuestionService.getInstance().getQuestions());
        answerTable.setItems(AnswerService.getInstance().getAnswers());
    }


    @SuppressWarnings("unchecked")
    private <T> void fillTableColums(Class<T> sourceClass, TableView<T> tableView) {
        FieldUtils.getFieldsListWithAnnotation(sourceClass, Column.class)
                .forEach(field -> {
                    field.setAccessible(true);
                    Column annotation = field.getAnnotation(Column.class);
                    TableColumn nextColumn = null;
                    switch (annotation.type()) {
                        case NUMBER: {
                            TableColumn<T, Number> column = new TableColumn<>();
                            column.setCellValueFactory(param -> {
                                try {
                                    return (ObservableValue<Number>) FieldUtils.readField(field, param.getValue());
                                } catch (IllegalAccessException ignored) {
                                }
                                return new SimpleIntegerProperty(Integer.MIN_VALUE);
                            });
                            nextColumn = column;
                            break;
                        }
                        case STRING: {
                            TableColumn<T, String> column = new TableColumn<>();
                            column.setCellValueFactory(param -> {
                                try {
                                    return (ObservableValue<String>) FieldUtils.readField(field, param.getValue());
                                } catch (IllegalAccessException ignored) {
                                }
                                return new SimpleStringProperty("bad data");
                            });
                            nextColumn = column;
                            break;
                        }
                        case OBJECT: {
                            TableColumn<T, String> column = new TableColumn<>();
                            column.setCellValueFactory(param -> {
                                try {
                                    ObjectProperty<Object> o = (ObjectProperty<Object>) FieldUtils.readField(field, param.getValue());
                                    return new SimpleStringProperty(o.getValue().toString());
                                } catch (IllegalAccessException ignored) {
                                }
                                return new SimpleStringProperty("bad data");
                            });
                            nextColumn = column;
                            break;
                        }
                        case BOOLEAN: {
                            TableColumn<T, String> column = new TableColumn<>();
                            column.setCellValueFactory(param -> {
                                try {
                                    Property<Boolean> o = (Property<Boolean>) FieldUtils.readField(field, param.getValue());
                                    return new SimpleStringProperty(o.getValue() ? "Да" : "Нет");
                                } catch (IllegalAccessException ignored) {
                                }
                                return new SimpleStringProperty("bad data");
                            });
                            nextColumn = column;
                            break;
                        }
                    }
                    Label label = new Label(annotation.name());
                    Tooltip tooltip = new Tooltip(annotation.description());
                    label.setTooltip(tooltip);
                    nextColumn.setGraphic(label);
                    tableView.getColumns().add(nextColumn);
                });
    }

    private void onDeleteButtonClick(ActionEvent event) {
        if (selectedItem != -1) {
            if (selectedCrud == 0) {
                PollService.getInstance().delete(selectedItem);
            } else if (selectedCrud == 1) {
                QuestionService.getInstance().delete(selectedItem);
            } else {
                AnswerService.getInstance().delete(selectedItem);
            }
        }
    }

    private void onEditButtonClick(ActionEvent event) {
        if (selectedItem == -1) {
            return;
        }
        Platform.runLater(() -> {
            InputDataController controller;
            if (selectedCrud == 0) {
                Poll selectedItem = pollTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, poll -> PollService.getInstance().save(poll));
            } else if (selectedCrud == 1) {
                Question selectedItem = questionTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, question -> QuestionService.getInstance().save(question));
            } else {
                Answer selectedItem = answerTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, answer -> AnswerService.getInstance().save(answer));
            }
            controller.initialize();
        });
        event.consume();
    }

    private void onNewResultButtonClick(ActionEvent event) {
        Platform.runLater(() -> {
            InputDataController controller;
            if (selectedCrud == 0) {
                controller = new InputDataController<>(new Poll(), poll -> PollService.getInstance().save(poll));
            } else if (selectedCrud == 1) {
                controller = new InputDataController<>(new Question(), question -> QuestionService.getInstance().save(question));
            } else {
                controller = new InputDataController<>(new Answer(), answer -> AnswerService.getInstance().save(answer));
            }
            controller.initialize();
        });
        event.consume();
    }

    private void onExportButtonClick(ActionEvent event) {
        updateControlStatus(false);
        File directory = directoryChooser.showDialog(MainApplication.getMainStage());
        if (directory != null) {
            ExportResultService service = new ExportResultService(getSelectedCrudData(), directory);
            service.setOnRunning(e -> spinner.setVisible(true));
            service.setOnSucceeded(e -> {
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.infoPoup(root, spinner, "Данные успешно экспортированы", 3);
            });
            service.setOnFailed(e -> {
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.warningPopup(root, spinner, "Ошибка при экспорте данных", 3);
            });
            service.start();
        } else {
            updateControlStatus(true);
        }
        event.consume();
    }

    public List<? extends Crud> getSelectedCrudData() {
        switch (selectedCrud) {
            case 0:
                return PollService.getInstance().getPolls();
            case 1:
                return QuestionService.getInstance().getQuestions();
            case 2:
                return AnswerService.getInstance().getAnswers();
            default:
                return PollService.getInstance().getPolls();
        }
    }

    private void updateControlStatus(boolean enable) {
        Stream.of(exportButton, newResultButton, deleteButton)
                .forEach(jfxButton -> jfxButton.setDisable(!enable));
    }
}
