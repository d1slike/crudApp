package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.fontawesome.Icon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ru.disdev.MainApplication;
import ru.disdev.datasource.ValueSource;
import ru.disdev.entity.Crud;
import ru.disdev.entity.crud.*;
import ru.disdev.service.*;
import ru.disdev.tasks.ExportResultService;
import ru.disdev.utils.PopupUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static ru.disdev.utils.TableUtils.fillTableColumns;

public class MainController implements Controller {

    private static final FileChooser.ExtensionFilter CSV_FILER = new FileChooser.ExtensionFilter("CSV file", "*.csv");
    public TableView<Link> linkTable;
    public TableView<User> userTable;
    public JFXButton statisticButton;
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
        statisticButton.setVisible(false);
        statisticButton.setOnAction(this::onStatisticButtonClick);
        Icon trash = new Icon("TRASH");
        trash.setTextFill(Color.RED);
        deleteButton.setGraphic(trash);
        deleteButton.setOnAction(this::onDeleteButtonClick);
        tabs.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedCrud = newValue.intValue();
            Stream.of(pollTable, questionTable, answerTable, userTable, linkTable).forEach(tableView -> tableView.getSelectionModel().clearSelection());
        });
        tabs.getSelectionModel().select(0);
        Stream.of(pollTable, questionTable, answerTable, userTable, linkTable).forEach(tableView ->
                tableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                    selectedItem = newValue.intValue();
                    deleteButton.setVisible(selectedItem != -1);
                    editButton.setVisible(selectedItem != -1);
                    statisticButton.setVisible(selectedItem != -1);
                }));
        fillTableColumns(Poll.class, pollTable);
        fillTableColumns(Question.class, questionTable);
        fillTableColumns(Answer.class, answerTable);
        fillTableColumns(User.class, userTable);
        fillTableColumns(Link.class, linkTable);
        pollTable.setItems(PollService.getInstance().getPolls());
        questionTable.setItems(QuestionService.getInstance().getQuestions());
        answerTable.setItems(AnswerService.getInstance().getAnswers());
        linkTable.setItems(LinkService.getInstance().getLinks());
        userTable.setItems(UserService.getInstance().getUsers());
    }

    private void onDeleteButtonClick(ActionEvent event) {
        if (selectedItem != -1) {
            if (selectedCrud == 0) {
                PollService.getInstance().delete(selectedItem);
            } else if (selectedCrud == 1) {
                QuestionService.getInstance().delete(selectedItem);
            } else if (selectedCrud == 2) {
                AnswerService.getInstance().delete(selectedItem);
            } else if (selectedCrud == 3) {
                LinkService.getInstance().delete(selectedItem);
            } else {
                UserService.getInstance().delete(selectedItem);
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
                controller = new InputDataController<>(selectedItem, poll -> {
                    PollService.getInstance().save(poll);
                    ValueSource.update();
                });
            } else if (selectedCrud == 1) {
                Question selectedItem = questionTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, question -> {
                    QuestionService.getInstance().save(question);
                    ValueSource.update();
                });
            } else if (selectedCrud == 2) {
                Answer selectedItem = answerTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, answer -> {
                    AnswerService.getInstance().save(answer);
                    ValueSource.update();
                });
            } else if (selectedCrud == 3) {
                Link link = linkTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(link, result -> {
                    ValueSource.update();
                });
            } else {
                User selectedItem = userTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, user -> {
                    UserService.getInstance().save(user);
                    ValueSource.update();
                });
            }
            controller.initialize();
        });
        event.consume();
    }

    private void onStatisticButtonClick(ActionEvent event) {
        if (selectedCrud == 1 && selectedItem != -1) {
            List<QuestionStatistic> questionStatistic = LinkService.getInstance()
                    .getQuestionStatistic(questionTable.getSelectionModel().getSelectedItem().getId());

            ResultTableController<QuestionStatistic> controller =
                    new ResultTableController<>(questionStatistic, "Статистика ответов по вопросу", QuestionStatistic.class);
            controller.initialize();
        }
    }

    private void onNewResultButtonClick(ActionEvent event) {
        if (selectedCrud == 1 && PollService.getInstance().getPolls().isEmpty()) {
            PopupUtils.warningPopup(root, spinner, "Необходимо добавить опросы!", 3);
            return;
        } else if (selectedCrud == 2 && QuestionService.getInstance().getQuestions().isEmpty()) {
            PopupUtils.warningPopup(root, spinner, "Необходимо добавить вопросы!", 3);
            return;
        } else if (selectedCrud == 3
                && (AnswerService.getInstance().getAnswers().isEmpty()
                || UserService.getInstance().getUsers().isEmpty())) {
            PopupUtils.warningPopup(root, spinner, "Необходимо добавить опрошенных и ответа!", 3);
            return;
        }
        Platform.runLater(() -> {
            InputDataController controller;
            if (selectedCrud == 0) {
                controller = new InputDataController<>(new Poll(), poll -> PollService.getInstance().save(poll));
            } else if (selectedCrud == 1) {
                controller = new InputDataController<>(new Question(), question -> QuestionService.getInstance().save(question));
            } else if (selectedCrud == 2) {
                controller = new InputDataController<>(new Answer(), answer -> AnswerService.getInstance().save(answer));
            } else if (selectedCrud == 3) {
                controller = new InputDataController<>(new Link(), link -> LinkService.getInstance().save(link));
            } else {
                controller = new InputDataController<>(new User(), user -> UserService.getInstance().save(user));
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

    private List<? extends Crud> getSelectedCrudData() {
        switch (selectedCrud) {
            case 0:
                return PollService.getInstance().getPolls();
            case 1:
                return QuestionService.getInstance().getQuestions();
            case 2:
                return AnswerService.getInstance().getAnswers();
            case 3:
                return LinkService.getInstance().getLinks();
            case 4:
                return UserService.getInstance().getUsers();
            default:
                return UserService.getInstance().getUsers();
        }
    }

    private void updateControlStatus(boolean enable) {
        Stream.of(exportButton, newResultButton, deleteButton)
                .forEach(jfxButton -> jfxButton.setDisable(!enable));
    }
}
