package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.PollDAO;
import ru.disdev.entity.crud.Poll;
import ru.disdev.entity.crud.Question;

import java.util.UUID;

public class PollService implements Service {
    private static PollService ourInstance = new PollService();

    public static PollService getInstance() {
        return ourInstance;
    }

    private PollService() {
    }

    private final ObservableList<Poll> polls = FXCollections.observableArrayList();
    private final PollDAO pollDAO = new PollDAO();

    public ObservableList<Poll> getPolls() {
        return polls;
    }

    @Override
    public void load() {
        polls.addAll(pollDAO.load());
    }

    public void save(Poll poll) {
        if (poll.getId() != null) {
            polls.remove(poll);
        } else {
            poll.setId(UUID.randomUUID().toString());
        }
        polls.add(pollDAO.save(poll));
    }

    public void delete(int index) {
        Poll poll = polls.remove(index);
        if (poll != null) {
            pollDAO.delete(poll.getId());
            ObservableList<Question> questions = QuestionService.getInstance().getQuestions();
            for (int i = 0, questionsSize = questions.size(); i < questionsSize; i++) {
                Question question = questions.get(i);
                if (question.getPollId().getValue().equals(poll.getId())) {
                    QuestionService.getInstance().delete(i);
                }
            }
        }
    }

}
