package ru.disdev.dao;

import ru.disdev.entity.crud.Poll;

import java.sql.Date;
import java.util.List;

import static ru.disdev.utils.FilterUtilsKt.toQuery;

public class PollDAO extends DAO<Poll> {

    @Override
    public List<Poll> find(Object filter) {
        return helper.query("SELECT * FROM poll" + toQuery(filter), (rs, index) -> {
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
        helper.update("INSERT INTO poll(id, title, category, start_date, end_date) VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
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
        return helper.update("DELETE FROM poll WHERE id = ?", id) > 0;
    }
}
