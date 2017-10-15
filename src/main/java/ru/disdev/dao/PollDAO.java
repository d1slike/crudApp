package ru.disdev.dao;

import ru.disdev.entity.crud.Poll;

import java.sql.Date;
import java.util.List;

public class PollDAO extends DAO<Poll> {

    @Override
    public List<Poll> load() {
        return helper.queryForList("SELECT * FROM poll", rs -> {
            Poll poll = new Poll();
            poll.setId(rs.getString("id"));
            poll.setTitle(rs.getString("title"));
            poll.setCategory(rs.getString("category"));
            poll.setEndDate(rs.getTimestamp("end_date").toLocalDateTime().toLocalDate());
            poll.setStartDate(rs.getTimestamp("start_date").toLocalDateTime().toLocalDate());
            return poll;
        });
    }

    @Override
    public Poll save(Poll poll) {
        helper.execute("INSERT INTO poll(id, title, category, start_date, end_date) VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                        "title=?, category=?, start_date=?, end_date=?",
                poll.getId(),
                poll.getTitle(),
                poll.getCategory(),
                Date.valueOf(poll.getStartDate()),
                Date.valueOf(poll.getEndDate()),
                poll.getTitle(),
                poll.getCategory(),
                Date.valueOf(poll.getStartDate()),
                Date.valueOf(poll.getEndDate()));
        return poll;
    }

    @Override
    public boolean delete(String id) {
        return helper.execute("DELETE FROM poll WHERE id = ?", id) > 0;
    }
}
