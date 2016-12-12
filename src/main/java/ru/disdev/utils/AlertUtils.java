package ru.disdev.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import ru.disdev.MainApplication;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AlertUtils {

    public static void showMessageAndCloseProgram(String header, String message) {
        Alert alert = prepareErrorAlertWindow();
        alert.setHeaderText(header);
        alert.setContentText(message);
        showAlert(alert);
    }

    public static void showMessageAndCloseProgram(Throwable ex) {
        Alert alert = prepareErrorAlertWindow();
        alert.setHeaderText(ex.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("StackTrace:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        showAlert(alert);
    }

    private static Alert prepareErrorAlertWindow() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(MainApplication.PROGRAM_NAME + " - CriticalError");
        alert.setOnCloseRequest(event -> Platform.exit());
        alert.initModality(Modality.APPLICATION_MODAL);
        /*if (MainApplication.getIcon() != null) {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(MainApplication.getIcon());
        }*/
        return alert;
    }

    private static void showAlert(Alert alert) {
        alert.showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .ifPresent(buttonType -> Platform.exit());
    }

}
