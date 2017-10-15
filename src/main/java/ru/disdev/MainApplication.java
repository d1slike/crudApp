package ru.disdev;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.disdev.controller.Controller;
import ru.disdev.utils.AlertUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MainApplication extends Application {

    public static final String PROGRAM_NAME = "CRUD Application";

    private static Stage mainStage;
    private static State currentState;
    private static Image icon;
    private static List<String> styles;

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
        styles = Arrays.asList(
                MainApplication.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
                MainApplication.class.getResource("/css/jfoenix-design.css").toExternalForm(),
                MainApplication.class.getResource("/css/jfoenix-main-demo.css").toExternalForm()
        );
        nextState();
    }

    public static void prevState() {
        if (currentState == null) return;
        int current = currentState.ordinal();
        if (current == 0) return;
        currentState = currentState.prev();
        updateState(null);
    }

    public static void nextState() {
        nextState(null);
    }

    public static void nextState(Object payload) {
        currentState = currentState == null ? State.UPDATE : currentState.next();
        updateState(null);
    }

    private static void updateState(Object payload) {
        try {
            FXMLLoader loader = new FXMLLoader(getFXMLUrl(currentState.fxmlName));
            Pane pane = loader.load();
            Controller controller = loader.getController();
            if (controller != null) {
                controller.acceptData(payload);
            }
            mainStage.hide();
            //JFXDecorator decorator = new JFXDecorator(mainStage, pane);
            //decorator.setCustomMaximize(true);
            Scene scene = new Scene(pane);
            scene.getStylesheets().addAll(styles);
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

    public static Stage newChildStage(String name) {
        Stage childStage = new Stage();
        childStage.initOwner(mainStage);
        String title = PROGRAM_NAME;
        if (name != null) {
            title = title + " - " + name;
        }
        childStage.setTitle(title);
        childStage.getIcons().add(icon);
        return childStage;
    }

    public static Scene newScene(Stage stage, Parent root, Runnable onClose) {
        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setCustomMaximize(true);
        if (onClose != null) {
            decorator.setOnCloseButtonAction(onClose);
        }
        Scene scene = new Scene(decorator);
        scene.getStylesheets().addAll(styles);
        return scene;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static URL getFXMLUrl(String fileName) {
        return MainApplication.class.getResource("/fxml/" + fileName);
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

        public State prev() {
            return ordinal() == 0 ? this : values()[ordinal() - 1];
        }

        public Consumer<Stage> getStageConfigurationCallback() {
            return stageConfigurationCallback;
        }
    }
}
