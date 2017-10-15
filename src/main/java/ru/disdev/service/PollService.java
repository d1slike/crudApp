package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.PollDAO;
import ru.disdev.entity.crud.Poll;

import java.util.UUID;

public class PollService implements Service {
    private static PollService ourInstance = new PollService();

    public static PollService getInstance() {
        return ourInstance;
    }

    private PollService() {
    }

    private ObservableList<Poll> polls = FXCollections.observableArrayList();
    private final PollDAO pollDAO = new PollDAO();

    public ObservableList<Poll> getPolls() {
        polls = FXCollections.observableArrayList(pollDAO.load());
        return polls;
    }

    @Override
    public void load() {

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
        }
    }

}
