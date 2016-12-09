package ru.disdev.entity.crud;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.disdev.entity.Crud;

public class Answer extends Crud {
    private StringProperty title = new SimpleStringProperty();
    private IntegerProperty number = new SimpleIntegerProperty();
    private StringProperty questionId = new SimpleStringProperty();

    public String getQuestionId() {
        return questionId.get();
    }

    public StringProperty questionIdProperty() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId.set(questionId);
    }

    public int getNumber() {
        return number.get();
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
}
