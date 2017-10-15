package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.QuestionDAO;
import ru.disdev.datasource.ValueSource;
import ru.disdev.entity.ForeignKey;
import ru.disdev.entity.crud.Question;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QuestionService implements Service {
    private static QuestionService ourInstance = new QuestionService();

    public static QuestionService getInstance() {
        return ourInstance;
    }

    private QuestionService() {
    }

    private ObservableList<Question> questions = FXCollections.observableArrayList();
    private final QuestionDAO questionDAO = new QuestionDAO();

    public ObservableList<Question> getQuestions() {
        List<Question> list = questionDAO.load();
        Map<String, ForeignKey> pollId = ValueSource.pollId();
        list.forEach(question -> {
            ForeignKey foreignKey = question.getPollId();
            if (pollId.containsKey(foreignKey.getValue())) {
                question.setPollId(pollId.get(foreignKey.getValue()));
            }
        });
        questions = FXCollections.observableArrayList(list);
        return questions;
    }

    @Override
    public void load() {
        //questions.addAll(questionDAO.load());
    }

    public void save(Question question) {
        if (question.getId() != null) {
            questions.remove(question);
        } else {
            question.setId(UUID.randomUUID().toString());
        }
        questions.add(questionDAO.save(question));
    }

    public void delete(int index) {
        Question question = questions.remove(index);
        if (question != null) {
            questionDAO.delete(question.getId());
        }
    }
}
