package ru.disdev.entity.crud;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.disdev.entity.Crud;

public class Question extends Crud {
    private StringProperty title = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty pollId = new SimpleStringProperty();

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getPollId() {
        return pollId.get();
    }

    public StringProperty pollIdProperty() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId.set(pollId);
    }
}
