package ru.disdev.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.entity.Column;

public class TableUtils {
    @SuppressWarnings("unchecked")
    public static <T> void fillTableColumns(Class<T> sourceClass, TableView<T> tableView) {
        FieldUtils.getFieldsListWithAnnotation(sourceClass, Column.class)
                .forEach(field -> {
                    field.setAccessible(true);
                    Column annotation = field.getAnnotation(Column.class);
                    TableColumn nextColumn = null;
                    switch (annotation.type()) {
                        case DOUBLE:
                        case INTEGER: {
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
                    if (nextColumn != null) {
                        Label label = new Label(annotation.name());
                        label.setTextAlignment(TextAlignment.CENTER);
                        Tooltip tooltip = new Tooltip(annotation.description());
                        label.setTooltip(tooltip);
                        nextColumn.setGraphic(label);
                        if (annotation.width() == Region.USE_COMPUTED_SIZE) {
                            nextColumn.setPrefWidth(label.getText().length() * 10);
                        } else {
                            nextColumn.setPrefWidth(annotation.width());
                        }
                        tableView.getColumns().add(nextColumn);
                    }
                });
    }
}
