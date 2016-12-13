package ru.disdev.entity.crud;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.disdev.entity.Column;
import ru.disdev.entity.Type;

public class QuestionStatistic {
    @Column(name = "Ответ", type = Type.STRING)
    private StringProperty answer = new SimpleStringProperty();
    @Column(name = "%", type = Type.STRING)
    private StringProperty statistic = new SimpleStringProperty();

    public String getAnswer() {
        return answer.get();
    }

    public StringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public String getStatistic() {
        return statistic.get();
    }

    public StringProperty statisticProperty() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic.set(statistic);
    }
}
