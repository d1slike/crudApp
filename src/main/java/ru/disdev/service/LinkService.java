package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.LinkDAO;
import ru.disdev.entity.crud.Link;
import ru.disdev.entity.crud.QuestionStatistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LinkService implements Service {
    private static LinkService ourInstance = new LinkService();

    public static LinkService getInstance() {
        return ourInstance;
    }

    private LinkService() {
    }

    private final ObservableList<Link> links = FXCollections.observableArrayList();
    private final LinkDAO linkDAO = new LinkDAO();

    @Override
    public void load() {
        links.addAll(linkDAO.load());
    }

    public void save(Link link) {
        links.add(link);
        linkDAO.save(link);
    }

    @Override
    public void delete(int index) {
        Link link = links.remove(index);
        if (link != null) {
            linkDAO.delete(link);
        }
    }

    public List<QuestionStatistic> getQuestionStatistic(String questionId) {
        Integer questionAnswerCount = linkDAO.getTotalQuestionAnswerCount(questionId);
        if (questionAnswerCount == null || questionAnswerCount == 0) {
            return new ArrayList<>();
        }
        List<QuestionStatistic> questionStatistics = linkDAO.getAnswerCount(questionId, questionAnswerCount);
        Set<String> ids = questionStatistics.stream()
                .map(QuestionStatistic::getAnswer)
                .collect(Collectors.toSet());
        Map<String, String> idTitleMap = AnswerService.getInstance().getIdTitleMap(ids);
        questionStatistics.stream().filter(statistic -> idTitleMap.containsKey(statistic.getAnswer()))
                .forEach(statistic -> statistic.setAnswer(idTitleMap.get(statistic.getAnswer())));
        return questionStatistics;
    }

    public ObservableList<Link> getLinks() {
        return links;
    }
}
