package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.QuestionDAO;
import ru.disdev.entity.crud.Answer;
import ru.disdev.entity.crud.Question;

import java.util.UUID;

public class QuestionService implements Service {
    private static QuestionService ourInstance = new QuestionService();

    public static QuestionService getInstance() {
        return ourInstance;
    }

    private QuestionService() {
    }

    private final ObservableList<Question> questions = FXCollections.observableArrayList();
    private final QuestionDAO questionDAO = new QuestionDAO();

    public ObservableList<Question> getQuestions() {
        return questions;
    }

    @Override
    public void load() {
        questions.addAll(questionDAO.load());
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
            ObservableList<Answer> answers = AnswerService.getInstance().getAnswers();
            for (int i = 0, answersSize = answers.size(); i < answersSize; i++) {
                Answer answer = answers.get(i);
                if (answer.getQuestionId().getValue().equals(question.getId())) {
                    AnswerService.getInstance().delete(i);
                }
            }
        }
    }
}
