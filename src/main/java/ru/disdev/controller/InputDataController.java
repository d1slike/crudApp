package ru.disdev.controller;

import com.jfoenix.controls.*;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import ru.disdev.MainApplication;
import ru.disdev.components.JFXPassword;
import ru.disdev.entity.Enum;
import ru.disdev.entity.ForeignKey;
import ru.disdev.entity.Type;
import ru.disdev.entity.ValueSource;
import ru.disdev.entity.input.*;
import ru.disdev.entity.input.conditional.Condition;
import ru.disdev.entity.input.conditional.DependOn;
import ru.disdev.entity.input.conditional.ElementsList;
import ru.disdev.utils.AlertUtils;
import ru.disdev.utils.FieldValidatorUtils;
import ru.disdev.utils.ParseUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static ru.disdev.utils.FieldValidatorUtils.getRangeValidator;
import static ru.disdev.utils.FieldValidatorUtils.getRequiredFieldValidator;

public class InputDataController<T> implements Controller {

    private static final int ELEMENTS_IN_COLUMN = 8;

    private T data;
    private String buttonText;
    private String title;
    private List<JFXTextField> fields = new ArrayList<>();
    private Map<Integer, ElementsList> stateMap = new HashMap<>();
    private Consumer<T> closeCallback;
    private Function<T, Boolean> syncValidation;

    public InputDataController(T data, Consumer<T> closeCallback) {
        this(data, "СОХРАНИТЬ", closeCallback);
    }

    public InputDataController(T data, String buttonText, Consumer<T> closeCallback) {
        this(data, buttonText, null, null, closeCallback);
    }

    public InputDataController(T data,
                               String buttonText,
                               String title,
                               Function<T, Boolean> syncValidation,
                               Consumer<T> closeCallback) {
        this.buttonText = buttonText;
        this.data = data;
        this.title = title;
        this.syncValidation = syncValidation;
        this.closeCallback = closeCallback;
    }

    @Override
    public void initialize() {

    }

    public StackPane show() {
        return show(null);
    }

    public StackPane show(Runnable closeHandler) {
        Stage stage = MainApplication.newChildStage(title);
        stage.initModality(Modality.WINDOW_MODAL);
        GridPane content = new GridPane();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        BorderPane root = new BorderPane(content);
        root.setBottom(makeCalcButton(stage));
        mapContent(content);
        StackPane pane = new StackPane(root);
        stage.setScene(MainApplication.newScene(stage, pane, closeHandler));
        stage.sizeToScene();
        stage.centerOnScreen();
        stateMap.forEach((state, list) -> {
            list.getDisable().forEach(node -> node.setDisable(false));
            list.getEnable().forEach(node -> node.setDisable(true));
        });
        stage.show();
        return pane;
    }

    private Node makeCalcButton(Stage stage) {
        JFXButton calcButton = new JFXButton(buttonText);
        calcButton.setButtonType(JFXButton.ButtonType.FLAT);
        calcButton.setOnAction(event -> {
            boolean checked = fields.stream()
                    .allMatch(jfxTextField -> jfxTextField.getParent().isDisable() || jfxTextField.validate());
            if (checked && syncValidation != null) {
                checked = syncValidation.apply(data);
            }
            if (checked) {
                FieldUtils.getAllFieldsList(data.getClass())
                        .forEach(field -> {
                            try {
                                field.setAccessible(true);
                                Property property = (Property) field.get(data);
                                property.unbind();
                            } catch (IllegalAccessException ignored) {

                            }
                        });
                fields.clear();
                stage.close();
                if (closeCallback != null) {
                    closeCallback.accept(data);
                    data = null;
                }
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
            for (Field field : FieldUtils.getAllFields(data.getClass())) {
                field.setAccessible(true);
                Region nextElement = null;
                if (field.isAnnotationPresent(TextField.class)) {
                    nextElement = mapTextField(field.getAnnotation(TextField.class), field);
                } else if (field.isAnnotationPresent(CheckBox.class)) {
                    nextElement = mapCheckBox(field.getAnnotation(CheckBox.class), field);
                } else if (field.isAnnotationPresent(ComboBox.class)) {
                    nextElement = mapComboBox(field.getAnnotation(ComboBox.class), field);
                } else if (field.isAnnotationPresent(DatePicker.class)) {
                    nextElement = mapDatePicker(field.getAnnotation(DatePicker.class), field);
                }
                if (nextElement != null) {
                    if (row == ELEMENTS_IN_COLUMN) {
                        column++;
                        row = 0;
                    }
                    if (field.isAnnotationPresent(DependOn.class)) {
                        DependOn dependOn = field.getAnnotation(DependOn.class);
                        ElementsList elementsList =
                                stateMap.computeIfAbsent(dependOn.id(), k -> new ElementsList());
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
        Type type = annotation.type();
        JFXTextField textField = type == Type.PASSWORD
                ? new JFXPassword()
                : new JFXTextField();
        textField.setPromptText(annotation.name());
        if (!annotation.description().isEmpty()) {
            textField.setTooltip(new Tooltip(annotation.description()));
        }
        textField.setAlignment(Pos.BOTTOM_LEFT);
        textField.setLabelFloat(true);
        box.getChildren().add(textField);
        if (annotation.isRequired()) {
            textField.setValidators(getRequiredFieldValidator());
        }
        if (field.isAnnotationPresent(Valid.class)) {
            Valid valid = field.getAnnotation(Valid.class);
            textField.getValidators().add(getRangeValidator(valid.min(), valid.max()));
        }
        textField.textProperty().addListener((observable, oldValue, newValue) -> textField.validate());
        switch (type) {
            case DOUBLE:
            case INTEGER:
                textField.setTextFormatter(FieldValidatorUtils.getNumericTextFilter());
                try {
                    textField.setText(((Property<Number>) FieldUtils.readField(field, data)).getValue().toString());
                } catch (Exception ignored) {
                }
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    Number value;
                    if (type == Type.INTEGER) {
                        value = ParseUtils.parseInt(newValue).orElse(null);
                    } else {
                        value = ParseUtils.parseDouble(newValue).orElse(null);
                    }
                    if (value != null) {
                        Property<Number> numberProperty = null;
                        try {
                            numberProperty = (Property<Number>) FieldUtils.readField(field, data);
                        } catch (Exception ignored) {
                        }
                        if (numberProperty != null) {
                            numberProperty.setValue(value);
                        }
                    }
                });
                break;
            case STRING:
            case PASSWORD:
                textField.textProperty()
                        .bindBidirectional((Property<String>) FieldUtils.readField(field, data));
                break;
        }
        fields.add(textField);
        return box;
    }

    @SuppressWarnings("unchecked")
    private JFXCheckBox mapCheckBox(CheckBox checkBox, Field field) throws IllegalAccessException {
        JFXCheckBox box = new JFXCheckBox();
        box.setText(checkBox.name());
        if (!checkBox.description().isEmpty()) {
            box.setTooltip(new Tooltip(checkBox.description()));
        }
        box.selectedProperty().bindBidirectional((Property<Boolean>) FieldUtils.readField(field, data));
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
    private HBox mapComboBox(ComboBox comboBox, Field field) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        HBox box = new HBox();
        JFXComboBox newBox = null;
        if (field.isAnnotationPresent(Enum.class)) {
            Class clazz = field.getAnnotation(Enum.class).value();
            if (clazz.isEnum()) {
                newBox = new JFXComboBox();
                newBox.getItems().addAll(clazz.getEnumConstants());
                newBox.valueProperty().bindBidirectional((Property) FieldUtils.readField(field, data));
                Object value = newBox.valueProperty().getValue();
                if (value == null && comboBox.defaultFirst()) {
                    newBox.setValue(newBox.getItems().get(0));
                }
            }
        } else if (field.isAnnotationPresent(ValueSource.class)) {
            String methodName = field.getAnnotation(ValueSource.class).methodName();
            Map<String, ForeignKey> items =
                    (Map<String, ForeignKey>) MethodUtils.invokeStaticMethod(ru.disdev.datasource.ValueSource.class, methodName);
            if (items != null && !items.isEmpty()) {
                newBox = new JFXComboBox();
                newBox.getItems().addAll(items.values());
                Property<ForeignKey> property
                        = (Property<ForeignKey>) FieldUtils.readField(field, data);
                newBox.valueProperty().bindBidirectional(property);
                if (property.getValue() == null) {
                    if (comboBox.defaultFirst()) {
                        newBox.setValue(newBox.getItems().get(0));
                    }
                } else if (items.containsKey(property.getValue().getValue())) {
                    property.setValue(items.get(property.getValue().getValue()));
                }
            }
        }
        if (newBox != null) {
            Tooltip tooltip = null;
            if (!comboBox.description().isEmpty()) {
                tooltip = new Tooltip(comboBox.description());
            }
            Label label = new Label(comboBox.name());
            label.setLabelFor(newBox);
            if (tooltip != null) {
                newBox.setTooltip(tooltip);
                label.setTooltip(tooltip);
            }
            label.setAlignment(Pos.CENTER_LEFT);
            label.setPadding(new Insets(0, 10, 0, 0));
            box.getChildren().addAll(label, newBox);
        }
        return box;
    }

    @SuppressWarnings("unchecked")
    private HBox mapDatePicker(DatePicker picker, Field field) throws IllegalAccessException {
        HBox hBox = new HBox();
        JFXDatePicker datePicker = new JFXDatePicker();
        datePicker.getEditor().setEditable(false);
        Property<LocalDate> property = (Property<LocalDate>) field.get(data);
        datePicker.valueProperty().bindBidirectional(property);
        if (property.getValue() == null) {
            property.setValue(LocalDate.now());
        }
        datePicker.setValue(property.getValue());
        Label label = new Label(picker.name());
        Tooltip tooltip = null;
        if (!picker.description().isEmpty()) {
            tooltip = new Tooltip(picker.description());
        }
        if (tooltip != null) {
            label.setTooltip(tooltip);
            datePicker.setTooltip(tooltip);
        }
        label.setLabelFor(datePicker);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPadding(new Insets(0, 10, 0, 0));
        hBox.getChildren().addAll(label, datePicker);
        return hBox;
    }

}
