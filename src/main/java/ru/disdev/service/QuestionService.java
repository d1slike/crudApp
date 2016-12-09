package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.datasource.DataSourceFactory;
import ru.disdev.entity.crud.Question;
import ru.disdev.jdbchelper.JdbcHelper;

import java.util.ArrayList;

public class QuestionService implements Service {
    private static QuestionService ourInstance = new QuestionService();

    public static QuestionService getInstance() {
        return ourInstance;
    }

    private QuestionService() {
    }

    private final ObservableList<Question> questions = FXCollections.observableArrayList();
    private final JdbcHelper helper = DataSourceFactory.getInstance().getHelper();

    public ObservableList<Question> getQuestions() {
        return questions;
    }

    @Override
    public void load() {
        ArrayList<Question> questions = helper.queryForList("SELECT * FROM question", rs -> {
            Question question = new Question();
            question.setId(rs.getString("id"));
            question.setTitle(rs.getString("title"));
            question.setDescription(rs.getString("description"));
            question.setPollId(rs.getString("poll_id"));
            return question;
        });
        this.questions.addAll(questions);
    }

    public void save(Question question) {
        questions.add(question);
    }

    public void delete(int index) {

    }
}
