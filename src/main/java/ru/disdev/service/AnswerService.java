package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.datasource.DataSourceFactory;
import ru.disdev.entity.crud.Answer;
import ru.disdev.jdbchelper.JdbcHelper;

import java.util.ArrayList;

public class AnswerService implements Service {
    private static AnswerService ourInstance = new AnswerService();

    public static AnswerService getInstance() {
        return ourInstance;
    }

    private AnswerService() {
    }

    private final ObservableList<Answer> answers = FXCollections.observableArrayList();
    private final JdbcHelper helper = DataSourceFactory.getInstance().getHelper();

    public ObservableList<Answer> getAnswers() {
        return answers;
    }

    @Override
    public void load() {
        ArrayList<Answer> answers = helper.queryForList("SELECT * FROM answer", rs -> {
            Answer answer = new Answer();
            answer.setTitle(rs.getString("title"));
            answer.setNumber(rs.getInt("number"));
            answer.setQuestionId(rs.getString("question_id"));
            return answer;
        });
        this.answers.addAll(answers);
    }

    public void save(Answer answer) {
        answers.add(answer);
    }

    public void delete(int idex) {

    }
}
