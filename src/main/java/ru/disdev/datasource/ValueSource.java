package ru.disdev.datasource;

import ru.disdev.entity.ForeignKey;
import ru.disdev.service.*;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ValueSource {
    public static Map<String, ForeignKey> pollId() {
        return PollService.getInstance().getPolls().stream()
                .map(poll -> new ForeignKey(poll.getId(), poll.getTitle()))
                .collect(toMap(ForeignKey::getValue, foreignKey -> foreignKey));
    }

    public static Map<String, ForeignKey> questionId() {
        return QuestionService.getInstance().getQuestions().stream()
                .map(question -> new ForeignKey(question.getId(), question.getTitle()))
                .collect(toMap(ForeignKey::getValue, foreignKey -> foreignKey));
    }

    public static Map<String, ForeignKey> userId() {
        return UserService.getInstance().getUsers().stream()
                .map(user -> new ForeignKey(user.getId(), user.getFio()))
                .collect(toMap(ForeignKey::getValue, foreignKey -> foreignKey));
    }

    public static Map<String, ForeignKey> answerId() {
        return AnswerService.getInstance().getAnswers().stream()
                .map(answer -> new ForeignKey(answer.getId(), answer.getTitle()))
                .collect(toMap(ForeignKey::getValue, foreignKey -> foreignKey));
    }

    public static void update() {
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
    }
}
