package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.datasource.DataSourceFactory;
import ru.disdev.entity.crud.Poll;
import ru.disdev.jdbchelper.JdbcHelper;

import java.util.ArrayList;
import java.util.UUID;

public class PollService implements Service {
    private static PollService ourInstance = new PollService();

    public static PollService getInstance() {
        return ourInstance;
    }

    private PollService() {
    }

    private final ObservableList<Poll> polls = FXCollections.observableArrayList();
    private final JdbcHelper helper = DataSourceFactory.getInstance().getHelper();

    public ObservableList<Poll> getPolls() {
        return polls;
    }

    @Override
    public void load() {
        ArrayList<Poll> polls = helper.queryForList("SELECT * FROM poll", rs -> {
            Poll poll = new Poll();
            poll.setTitle(rs.getString("title"));
            poll.setCategory(rs.getString("category"));
            poll.setEndDate(rs.getTimestamp("end_date").toString());
            poll.setStartDate(rs.getTimestamp("start_date").toString());
            return poll;
        });
        this.polls.addAll(polls);
    }

    public void save(Poll poll) {
        if (poll.getId() != null) {
            polls.remove(poll);
        } else {
            poll.setId(UUID.randomUUID().toString());
        }
        saveInDb(poll);
        polls.add(poll);
    }

    public void delete(int index) {

    }

    private void saveInDb(Poll poll) {
        helper.execute("REPLACE INTO poll VALUES(?, ?, ?, ?, ?)",
                poll.getId(),
                poll.getTitle(),
                poll.getCategory(),
                poll.getStartDate(),
                poll.getEndDate()); //TODO дата !
    }
}
