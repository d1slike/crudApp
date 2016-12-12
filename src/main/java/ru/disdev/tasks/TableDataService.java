package ru.disdev.tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.disdev.datasource.ValueSource;
import ru.disdev.entity.ForeignKey;
import ru.disdev.service.*;

import java.util.Map;
import java.util.stream.Stream;

public class TableDataService extends Service<Void> {

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 6);
                int i[] = new int[1];
                Stream.of(LinkService.getInstance(), UserService.getInstance(),
                        PollService.getInstance(), AnswerService.getInstance(),
                        QuestionService.getInstance()).forEach(service -> {
                    service.load();
                    updateProgress(++i[0], 6);
                });
                Map<String, ForeignKey> answerId = ValueSource.answerId();
                Map<String, ForeignKey> pollId = ValueSource.pollId();
                Map<String, ForeignKey> questionId = ValueSource.questionId();
                Map<String, ForeignKey> userId = ValueSource.userId();
                QuestionService.getInstance().getQuestions().forEach(question -> {
                    ForeignKey foreignKey = question.getPollId();
                    if (pollId.containsKey(foreignKey.getValue())) {
                        question.setPollId(pollId.get(foreignKey.getValue()));
                    }
                });
                AnswerService.getInstance().getAnswers().forEach(answer -> {
                    ForeignKey foreignKey = answer.getQuestionId();
                    if (questionId.containsKey(foreignKey.getValue())) {
                        answer.setQuestionId(questionId.get(foreignKey.getValue()));
                    }
                });
                LinkService.getInstance().getLinks().forEach(link -> {
                    ForeignKey user = userId.get(link.getUser().getValue());
                    if (user != null) {
                        link.setUser(user);
                    }
                    ForeignKey answer = answerId.get(link.getAnswer().getValue());
                    if (answer != null) {
                        link.setAnswer(answer);
                    }
                    ForeignKey question = questionId.get(link.getQuestion().getValue());
                    if (question != null) {
                        link.setQuestion(question);
                    }
                });
                updateProgress(6, 6);
                return null;
            }

        };
    }

}

