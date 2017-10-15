package ru.disdev.datasource;

import ru.disdev.entity.ForeignKey;
import ru.disdev.service.AnswerService;
import ru.disdev.service.PollService;
import ru.disdev.service.QuestionService;
import ru.disdev.service.UserService;

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

}
