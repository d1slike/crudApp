package ru.disdev.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Crud {
    @Column(name = "Идентификатор", type = Type.STRING, description = "Идентификатор записи", width = 260)
    private StringProperty id = new SimpleStringProperty();

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }
}
