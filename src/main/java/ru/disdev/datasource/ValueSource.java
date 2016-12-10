package ru.disdev.datasource;

import ru.disdev.entity.ForeignKey;
import ru.disdev.service.PollService;
import ru.disdev.service.QuestionService;

import java.util.Map;
import java.util.stream.Collectors;

public class ValueSource {
    public static Map<String, ForeignKey> pollId() {
        return PollService.getInstance().getPolls().stream()
                .map(poll -> new ForeignKey(poll.getId(), poll.getTitle()))
                .collect(Collectors.toMap(ForeignKey::getValue, foreignKey -> foreignKey));
    }

    public static Map<String, ForeignKey> questionId() {
        return QuestionService.getInstance().getQuestions().stream()
                .map(question -> new ForeignKey(question.getId(), question.getTitle()))
                .collect(Collectors.toMap(ForeignKey::getValue, foreignKey -> foreignKey));
    }
}
