package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.fontawesome.Icon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import ru.disdev.MainApplication;
import ru.disdev.entity.*;
import ru.disdev.entity.crud.*;
import ru.disdev.service.*;
import ru.disdev.tasks.ExportResultService;
import ru.disdev.utils.PopupUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static ru.disdev.utils.TableUtils.fillTableColumns;

public class MainController implements Controller {

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
    private Object filter;
    @FXML
    private JFXButton filterButton;
    @FXML
    private JFXButton clearFilterButton;

    private final DirectoryChooser directoryChooser = new DirectoryChooser();

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
        deleteButton.setVisible(false);
        statisticButton.setVisible(false);
        statisticButton.setOnAction(this::onStatisticButtonClick);
        Icon trash = new Icon("TRASH");
        trash.setTextFill(Color.RED);
        deleteButton.setGraphic(trash);
        deleteButton.setOnAction(this::onDeleteButtonClick);
        clearFilterButton.setOnAction(e -> {
            filter = null;
            fetchData();
            clearFilterButton.setVisible(false);
        });
        tabs.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedCrud = newValue.intValue();
            filter = null;
            if (selectedCrud == 0) {
                filterButton.setOnAction(onClickFilter(PollsFilter::new));
            } else if (selectedCrud == 1) {
                filterButton.setOnAction(onClickFilter(QuestionsFilter::new));
            } else if (selectedCrud == 2) {
                filterButton.setOnAction(onClickFilter(AnswerFilter::new));
            } else if (selectedCrud == 3) {
                filterButton.setOnAction(onClickFilter(LinkFilter::new));
            } else {
                filterButton.setOnAction(onClickFilter(UserFilter::new));
            }
            Stream.of(pollTable, questionTable, answerTable, userTable, linkTable)
                    .forEach(tableView -> tableView.getSelectionModel().clearSelection());
            fetchData();
        });
        tabs.getSelectionModel().select(0);
        Stream.of(pollTable, questionTable, answerTable, userTable, linkTable).forEach(tableView ->
                tableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                    selectedItem = newValue.intValue();
                    deleteButton.setVisible(selectedItem != -1);
                    editButton.setVisible(selectedItem != -1 && selectedCrud != 3);
                    statisticButton.setVisible(selectedItem != -1);
                }));
        fillTableColumns(Poll.class, pollTable);
        fillTableColumns(Question.class, questionTable);
        fillTableColumns(Answer.class, answerTable);
        fillTableColumns(User.class, userTable);
        fillTableColumns(Link.class, linkTable);
        pollTable.setItems(PollService.getInstance().getPolls(null));
    }

    private EventHandler<ActionEvent> onClickFilter(Supplier supplier) {
        return event -> new InputDataController<>(supplier.get(), fullFilter -> {
            this.filter = fullFilter;
            clearFilterButton.setVisible(true);
            fetchData();
        }).show();
    }

    private void fetchData() {
        if (selectedCrud == 0) {
            CompletableFuture.supplyAsync(() -> PollService.getInstance().getPolls(filter))
                    .thenAccept(list -> Platform.runLater(() -> pollTable.setItems(list)));
        } else if (selectedCrud == 1) {
            CompletableFuture.supplyAsync(() -> QuestionService.getInstance().getQuestions(filter))
                    .thenAccept(list -> Platform.runLater(() -> questionTable.setItems(list)));
        } else if (selectedCrud == 2) {
            CompletableFuture.supplyAsync(() -> AnswerService.getInstance().getAnswers(filter))
                    .thenAccept(list -> Platform.runLater(() -> answerTable.setItems(list)));
        } else if (selectedCrud == 3) {
            CompletableFuture.supplyAsync(() -> LinkService.getInstance().getLinks(filter))
                    .thenAccept(list -> Platform.runLater(() -> linkTable.setItems(list)));
        } else {
            CompletableFuture.supplyAsync(() -> UserService.getInstance().getUsers(filter))
                    .thenAccept(list -> Platform.runLater(() -> userTable.setItems(list)));
        }
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
                });
            } else if (selectedCrud == 1) {
                Question selectedItem = questionTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, question -> {
                    QuestionService.getInstance().save(question);
                });
            } else if (selectedCrud == 2) {
                Answer selectedItem = answerTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, answer -> {
                    AnswerService.getInstance().save(answer);
                });
            } else if (selectedCrud == 3) {
                Link link = linkTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(link, result -> {

                });
            } else {
                User selectedItem = userTable.getSelectionModel().getSelectedItem();
                controller = new InputDataController<>(selectedItem, user -> {
                    UserService.getInstance().save(user);
                });
            }
            controller.show();
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
        if (selectedCrud == 1 && PollService.getInstance().getPolls(null).isEmpty()) {
            PopupUtils.warningPopup(root, "Необходимо добавить опросы!", 3);
            return;
        } else if (selectedCrud == 2 && QuestionService.getInstance().getQuestions(null).isEmpty()) {
            PopupUtils.warningPopup(root, "Необходимо добавить вопросы!", 3);
            return;
        } else if (selectedCrud == 3
                && (AnswerService.getInstance().getAnswers(null).isEmpty()
                || UserService.getInstance().getUsers(null).isEmpty())) {
            PopupUtils.warningPopup(root, "Необходимо добавить опрошенных и ответа!", 3);
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
            controller.show();
        });
        event.consume();
    }

    private void onExportButtonClick(ActionEvent event) {
        updateControlStatus(false);
        File directory = directoryChooser.showDialog(MainApplication.getMainStage());
        String fileName;
        if (selectedCrud == 0) {
            fileName = "Опросы";
        } else if (selectedCrud == 1) {
            fileName = "Вопросы";
        } else if (selectedCrud == 2) {
            fileName = "Ответы";
        } else if (selectedCrud == 3) {
            fileName = "Результаты";
        } else {
            fileName = "Пользватели";
        }
        if (directory != null) {
            ExportResultService service = new ExportResultService(getSelectedCrudData(), directory, fileName);
            service.setOnRunning(e -> spinner.setVisible(true));
            service.setOnSucceeded(e -> {
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.infoPopup(root, "Данные успешно экспортированы", 3);
            });
            service.setOnFailed(e -> {
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.warningPopup(root, "Ошибка при экспорте данных", 3);
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
                return PollService.getInstance().getPolls(null);
            case 1:
                return QuestionService.getInstance().getQuestions(null);
            case 2:
                return AnswerService.getInstance().getAnswers(null);
            case 3:
                return LinkService.getInstance().getLinks(null);
            case 4:
                return UserService.getInstance().getUsers(null);
            default:
                return UserService.getInstance().getUsers(null);
        }
    }

    private void updateControlStatus(boolean enable) {
        Stream.of(exportButton, newResultButton, deleteButton)
                .forEach(jfxButton -> jfxButton.setDisable(!enable));
    }
}
