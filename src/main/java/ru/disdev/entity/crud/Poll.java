package ru.disdev.entity.crud;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.disdev.entity.Column;
import ru.disdev.entity.Crud;
import ru.disdev.entity.Type;
import ru.disdev.entity.input.DatePicker;
import ru.disdev.entity.input.TextField;

import java.time.LocalDate;

public class Poll extends Crud {
    @TextField(name = "Название опроса", description = "Название опроса", type = Type.STRING)
    @Column(name = "Название опроса", description = "Название опроса", type = Type.STRING)
    private StringProperty title = new SimpleStringProperty();
    @TextField(name = "Категория", description = "Название категории", type = Type.STRING)
    @Column(name = "Категория", description = "Название категории", type = Type.STRING)
    private StringProperty category = new SimpleStringProperty();
    @DatePicker(name = "Дата начала", description = "Дата начала")
    @Column(name = "Дата начала", description = "Дата начала", type = Type.OBJECT)
    private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
    @DatePicker(name = "Дата окончания", description = "Дата окончания")
    @Column(name = "Дата окончания", description = "Дата окончания", type = Type.OBJECT)
    private ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }
}
