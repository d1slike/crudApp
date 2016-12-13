package ru.disdev.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import ru.disdev.MainApplication;
import ru.disdev.utils.TableUtils;

import java.util.List;
import java.util.stream.Stream;

public class ResultTableController<T> implements Controller {

    private final ObservableList<T> data = FXCollections.observableArrayList();
    private final TableView<T> table = new TableView<>();
    private final String header;
    private final Class<T> aClass;

    public ResultTableController(List<T> data, String header, Class<T> tClass) {
        this.data.addAll(data);
        this.header = header;
        this.aClass = tClass;
    }

    @Override
    public void initialize() {
        table.setItems(data);
        table.setPrefWidth(Region.USE_COMPUTED_SIZE);
        table.setPrefHeight(Region.USE_COMPUTED_SIZE);
        if (!data.isEmpty()) {
            TableUtils.fillTableColumns(aClass, table);
        }
        ScrollPane root = new ScrollPane(table);
        root.setPadding(new Insets(15));
        Stage stage = MainApplication.newChildStage();
        Label noResult = new Label("Нет резульатов");
        Label header = new Label(this.header);
        Stream.of(noResult, header).forEach(label -> {
            label.setPadding(new Insets(15));
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
        });
        BorderPane borderPane = new BorderPane(data.isEmpty() ? noResult : root);
        borderPane.setTop(header);
        stage.setScene(MainApplication.newScene(borderPane));
        stage.showAndWait();
    }
}
