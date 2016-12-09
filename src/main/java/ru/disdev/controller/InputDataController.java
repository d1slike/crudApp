package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.MainApplication;
import ru.disdev.entity.Crud;
import ru.disdev.entity.input.CheckBox;
import ru.disdev.entity.input.CheckBoxState;
import ru.disdev.entity.input.ComboBox;
import ru.disdev.entity.input.TextField;
import ru.disdev.entity.input.conditional.Condition;
import ru.disdev.entity.input.conditional.DependOn;
import ru.disdev.entity.input.conditional.ElementsList;
import ru.disdev.entity.input.Valid;
import ru.disdev.utils.AlertUtils;
import ru.disdev.utils.FieldValidatorUtils;
import ru.disdev.utils.NumberUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static ru.disdev.utils.FieldValidatorUtils.getRangeValidator;
import static ru.disdev.utils.FieldValidatorUtils.getRequiredFieldValidator;

public class InputDataController<T extends Crud> implements Controller {

    private static final int ELEMENTS_IN_COLUMN = 8;

    private T crud;
    private List<JFXTextField> fields = new ArrayList<>();
    private Map<Integer, ElementsList> stateMap = new HashMap<>();
    private final Consumer<T> closeCallback;

    public InputDataController(T crud, Consumer<T> closeCallback) {
        this.crud = crud;
        this.closeCallback = closeCallback;
    }

    @Override
    public void initialize() {
        Stage stage = MainApplication.newChildStage();
        stage.initModality(Modality.WINDOW_MODAL);
        GridPane content = new GridPane();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        BorderPane root = new BorderPane(content);
        root.setBottom(makeCalcButton(stage));
        mapContent(content);
        stage.setScene(MainApplication.newScene(root));
        stage.sizeToScene();
        stage.centerOnScreen();
        stateMap.forEach((state, list) -> {
            list.getDisable().forEach(node -> node.setDisable(false));
            list.getEnable().forEach(node -> node.setDisable(true));
        });
        stage.showAndWait();
    }

    private Node makeCalcButton(Stage stage) {
        JFXButton calcButton = new JFXButton("ДОБАВИТЬ");
        calcButton.setOnAction(event -> {
            boolean checked = fields.stream()
                    .allMatch(jfxTextField -> jfxTextField.getParent().isDisable() || jfxTextField.validate());
            if (checked) {
                closeCallback.accept(crud);
                stage.close();
            }
            event.consume();
        });
        calcButton.setAlignment(Pos.CENTER);
        HBox box = new HBox(calcButton);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void mapContent(GridPane contentPane) {
        int row = 0, column = 0;
        try {
            for (Field field : FieldUtils.getAllFields(crud.getClass())) {
                field.setAccessible(true);
                Region nextElement = null;
                if (field.isAnnotationPresent(TextField.class)) {
                    nextElement = mapTextField(field.getAnnotation(TextField.class), field);
                } else if (field.isAnnotationPresent(CheckBox.class)) {
                    nextElement = mapCheckBox(field.getAnnotation(CheckBox.class), field);
                } else if (field.isAnnotationPresent(ComboBox.class)) {
                    nextElement = mapComboBox(field.getAnnotation(ComboBox.class), field);
                }
                if (nextElement != null) {
                    if (row == ELEMENTS_IN_COLUMN) {
                        column++;
                        row = 0;
                    }
                    if (field.isAnnotationPresent(DependOn.class)) {
                        DependOn dependOn = field.getAnnotation(DependOn.class);
                        ElementsList elementsList = stateMap.get(dependOn.id());
                        if (elementsList == null) {
                            elementsList = new ElementsList();
                            stateMap.put(dependOn.id(), elementsList);
                        }
                        if (dependOn.showOn() == CheckBoxState.CHECKED) {
                            elementsList.addToEnable(nextElement);
                        } else {
                            elementsList.addToDisable(nextElement);
                        }
                    }
                    nextElement.setPadding(new Insets(15));
                    contentPane.add(nextElement, column, row++);
                }
            }
        } catch (Exception ex) {
            AlertUtils.showMessageAndCloseProgram(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private HBox mapTextField(TextField annotation, Field field) throws IllegalAccessException {
        HBox box = new HBox();
        JFXTextField textField = new JFXTextField();
        textField.setPromptText(annotation.name());
        textField.setTooltip(new Tooltip(annotation.description()));
        textField.setAlignment(Pos.BOTTOM_LEFT);
        textField.setStyle("-fx-label-float:true;");
        box.getChildren().add(textField);
        if (annotation.isRequired()) {
            textField.setValidators(getRequiredFieldValidator());
        }
        if (field.isAnnotationPresent(Valid.class)) {
            Valid valid = field.getAnnotation(Valid.class);
            textField.getValidators().add(getRangeValidator(valid.min(), valid.max()));
        }
        textField.textProperty().addListener((observable, oldValue, newValue) -> textField.validate());
        switch (annotation.type()) {
            case NUMBER:
                textField.setTextFormatter(FieldValidatorUtils.getNumericTextFilter());
                try {
                    textField.setText(((Property<Number>) FieldUtils.readField(field, crud)).getValue().toString());
                } catch (Exception ignored) {
                }
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    NumberUtils.parseDouble(newValue).ifPresent(value -> {
                        Property<Number> numberProperty = null;
                        try {
                            numberProperty = (Property<Number>) FieldUtils.readField(field, crud);
                        } catch (Exception ignored) {
                        }
                        if (numberProperty != null) {
                            numberProperty.setValue(value);
                        }
                    });
                });
                break;
            case STRING:
                textField.textProperty()
                        .bindBidirectional((Property<String>) FieldUtils.readField(field, crud));
                break;
        }
        fields.add(textField);
        return box;
    }

    @SuppressWarnings("unchecked")
    private JFXCheckBox mapCheckBox(CheckBox checkBox, Field field) throws IllegalAccessException {
        JFXCheckBox box = new JFXCheckBox();
        box.setText(checkBox.name());
        box.setTooltip(new Tooltip(checkBox.description()));
        box.selectedProperty().bindBidirectional((Property<Boolean>) FieldUtils.readField(field, crud));
        if (field.isAnnotationPresent(Condition.class)) {
            Condition condition = field.getAnnotation(Condition.class);
            if (!stateMap.containsKey(condition.value())) {
                stateMap.put(condition.value(), new ElementsList());
            }
            box.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!stateMap.containsKey(condition.value())) {
                    return;
                }
                ElementsList elementsList = stateMap.get(condition.value());
                elementsList.getEnable().forEach(node -> node.setDisable(!newValue));
                elementsList.getDisable().forEach(node -> node.setDisable(newValue));
            });
        }
        return box;
    }

    @SuppressWarnings("unchecked")
    private HBox mapComboBox(ComboBox comboBox, Field field) throws IllegalAccessException {
        HBox box = new HBox();
        JFXComboBox newBox = null;
        Class<?> aClass = comboBox.enumClass();
        if (aClass.isEnum()) {
            newBox = new JFXComboBox();
            newBox.getItems().addAll(aClass.getEnumConstants());
            newBox.valueProperty().bindBidirectional((Property) FieldUtils.readField(field, crud));
        }
        if (newBox != null) {
            newBox.setValue(newBox.getItems().get(0));
            Tooltip tooltip = new Tooltip(comboBox.description());
            newBox.setTooltip(tooltip);
            Label label = new Label(comboBox.name());
            label.setLabelFor(newBox);
            label.setTooltip(tooltip);
            label.setAlignment(Pos.CENTER_LEFT);
            label.setPadding(new Insets(0, 10, 0, 0));
            box.getChildren().addAll(label, newBox);
        }
        return box;
    }

}
