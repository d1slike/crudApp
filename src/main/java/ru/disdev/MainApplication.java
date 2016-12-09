package ru.disdev;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.disdev.utils.AlertUtils;

import java.net.URL;
import java.util.function.Consumer;

public class MainApplication extends Application {

    public static final String PROGRAM_NAME = "CRUD Application";

    private static Stage mainStage;
    private static State currentState;
    private static Image icon;
    private static String style;

    public static void main(String[] args) {
        launch(MainApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        //mainStage.initStyle(StageStyle.UNDECORATED); //TODO заменить на свои кнопки
        mainStage.setTitle(PROGRAM_NAME);
        icon = new Image(MainApplication.class.getResource("/icon.png").toExternalForm());
        primaryStage.getIcons().add(icon);
        style = MainApplication.class.getResource("/css/main.css").toExternalForm();
        nextState();
    }

    public static void nextState() {
        currentState = currentState == null ? State.UPDATE : currentState.next();
        try {
            URL url = MainApplication.class.getResource("/fxml/" + currentState.fxmlName);
            Pane pane = FXMLLoader.load(url);
            mainStage.hide();
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(style);
            mainStage.setScene(scene);
            mainStage.sizeToScene();
            mainStage.centerOnScreen();
            Consumer<Stage> stageConfigurationCallback = currentState.getStageConfigurationCallback();
            if (stageConfigurationCallback != null) {
                stageConfigurationCallback.accept(mainStage);
            }
            mainStage.show();
        } catch (Exception e) {
            AlertUtils.showMessageAndCloseProgram(e);
        }

    }

    public static Stage newChildStage() {
        Stage childStage = new Stage();
        childStage.initOwner(mainStage);
        childStage.setTitle(PROGRAM_NAME);
        childStage.getIcons().add(icon);
        return childStage;
    }

    public static Scene newScene(Parent root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(style);
        return scene;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    private enum State {
        UPDATE("update.fxml", stage -> {
            stage.setResizable(false);
            stage.setOnCloseRequest(Event::consume);
        }),
        MAIN("main.fxml", stage -> {
            stage.setResizable(true);
            stage.setOnCloseRequest(null);
        });

        private final String fxmlName;
        private Consumer<Stage> stageConfigurationCallback;

        State(String fxmlName, Consumer<Stage> stageConfigurationCallback) {
            this.fxmlName = fxmlName;
            this.stageConfigurationCallback = stageConfigurationCallback;
        }

        State(String fxmlName) {
            this.fxmlName = fxmlName;
        }

        public State next() {
            return this == values()[values().length - 1] ? this : values()[ordinal() + 1];
        }

        public Consumer<Stage> getStageConfigurationCallback() {
            return stageConfigurationCallback;
        }
    }
}
