package ru.disdev.entity.crud;

import javafx.beans.property.*;
import ru.disdev.entity.*;
import ru.disdev.entity.input.ComboBox;
import ru.disdev.entity.input.TextField;

public class Answer extends Crud {
    @TextField(name = "Текст ответа", description = "Текст ответа", type = Type.STRING)
    @Column(name = "Текст ответа", description = "Текст ответа", type = Type.STRING)
    private StringProperty title = new SimpleStringProperty();
    @TextField(name = "Номер ответа", description = "Номер ответа", type = Type.INTEGER, isRequired = false)
    @Column(name = "Номер ответа", description = "Номер ответа", type = Type.INTEGER)
    private IntegerProperty number = new SimpleIntegerProperty();
    @ComboBox(name = "Вопрос", description = "Текст вопроса")
    @Column(name = "Вопрос", description = "Текст вопроса", type = Type.OBJECT)
    @ValueSource(methodName = "questionId")
    private ObjectProperty<ForeignKey> questionId = new SimpleObjectProperty<>();

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

    public ForeignKey getQuestionId() {
        return questionId.get();
    }

    public ObjectProperty<ForeignKey> questionIdProperty() {
        return questionId;
    }

    public void setQuestionId(ForeignKey questionId) {
        this.questionId.set(questionId);
    }
}
