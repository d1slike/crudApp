package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.AnswerDAO;
import ru.disdev.entity.Crud;
import ru.disdev.entity.crud.Answer;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AnswerService implements Service {
    private static AnswerService ourInstance = new AnswerService();

    public static AnswerService getInstance() {
        return ourInstance;
    }

    private AnswerService() {
    }

    private final ObservableList<Answer> answers = FXCollections.observableArrayList();
    private final AnswerDAO answerDAO = new AnswerDAO();

    public ObservableList<Answer> getAnswers() {
        return answers;
    }

    @Override
    public void load() {
        answers.addAll(answerDAO.load());
    }

    public void save(Answer answer) {
        if (answer.getId() != null) {
            answers.remove(answer);
        } else {
            answer.setId(UUID.randomUUID().toString());
        }
        answers.add(answerDAO.save(answer));
    }

    public Map<String, String> getIdTitleMap(Set<String> ids) {
        return answerDAO.getAnswersByIds(String.join(",", ids)).stream()
                .collect(Collectors.toMap(Crud::getId, Answer::getTitle));
    }

    public void delete(int index) {
        Answer remove = answers.remove(index);
        if (remove != null) {
            answerDAO.delete(remove.getId());
        }
    }
}
