package ru.disdev.controller;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.disdev.MainApplication;
import ru.disdev.tasks.TableDataService;
import ru.disdev.utils.AlertUtils;


public class LoadController implements Controller {

    private static final String INITIAL_MESSAGE = "Загрузка данных";

    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private Label infoLabel;

    @Override
    public void initialize() {
        TableDataService service = new TableDataService();
        service.setOnSucceeded(event -> MainApplication.nextState());
        service.setOnFailed(event -> AlertUtils.showMessageAndCloseProgram((Exception) event.getSource().getException()));
        progressBar.progressProperty().bind(service.progressProperty());
        infoLabel.setText(INITIAL_MESSAGE);
        service.start();
    }
}
