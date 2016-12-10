package ru.disdev.entity.crud;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.disdev.entity.*;
import ru.disdev.entity.input.ComboBox;
import ru.disdev.entity.input.TextField;

public class Question extends Crud {
    @TextField(name = "Вопрос", description = "Заголовок вопроса", type = Type.STRING)
    @Column(name = "Вопрос", description = "Заголовок опроса", type = Type.STRING)
    private StringProperty title = new SimpleStringProperty();
    @TextField(name = "Дополнительно", description = "Допонительное описание опроса", type = Type.STRING)
    @Column(name = "Дополнительно", description = "Допонительное описание опроса", type = Type.STRING)
    private StringProperty description = new SimpleStringProperty();
    @ComboBox(name = "Опрос", description = "Заголовок опроса")
    @ValueSource(methodName = "pollId")
    @Column(name = "Опрос", description = "Заголовок опроса", type = Type.OBJECT, width = 260)
    private ObjectProperty<ForeignKey> pollId = new SimpleObjectProperty<>();

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

    public ForeignKey getPollId() {
        return pollId.get();
    }

    public ObjectProperty<ForeignKey> pollIdProperty() {
        return pollId;
    }

    public void setPollId(ForeignKey pollId) {
        this.pollId.set(pollId);
    }
}
