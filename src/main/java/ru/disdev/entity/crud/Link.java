package ru.disdev.entity.crud;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.disdev.entity.*;
import ru.disdev.entity.input.ComboBox;

public class Link extends Crud {
    @ComboBox(name = "Вопрос")
    @ValueSource(methodName = "questionId")
    @Column(name = "Вопрос", description = "Заголовок опроса", type = Type.OBJECT, width = 260)
    private ObjectProperty<ForeignKey> question = new SimpleObjectProperty<>();
    @ComboBox(name = "Ответ")
    @ValueSource(methodName = "answerId")
    @Column(name = "Ответ", description = "Заголовок ответа", type = Type.OBJECT, width = 260)
    private ObjectProperty<ForeignKey> answer = new SimpleObjectProperty<>();
    @ComboBox(name = "Опрошенный")
    @ValueSource(methodName = "userId")
    @Column(name = "Опрошенный", description = "Опрошенный", type = Type.OBJECT, width = 260)
    private ObjectProperty<ForeignKey> user = new SimpleObjectProperty<>();

    public ForeignKey getQuestion() {
        return question.get();
    }

    public ObjectProperty<ForeignKey> questionProperty() {
        return question;
    }

    public void setQuestion(ForeignKey question) {
        this.question.set(question);
    }

    public ForeignKey getAnswer() {
        return answer.get();
    }

    public ObjectProperty<ForeignKey> answerProperty() {
        return answer;
    }

    public void setAnswer(ForeignKey answer) {
        this.answer.set(answer);
    }

    public ForeignKey getUser() {
        return user.get();
    }

    public ObjectProperty<ForeignKey> userProperty() {
        return user;
    }

    public void setUser(ForeignKey user) {
        this.user.set(user);
    }
}
