package ru.disdev.dao;

import ru.disdev.entity.ForeignKey;
import ru.disdev.entity.crud.Question;

import java.util.List;

import static ru.disdev.utils.FilterUtilsKt.toQuery;

public class QuestionDAO extends DAO<Question> {

    @Override
    public List<Question> find(Object filter) {
        return helper.query("SELECT * FROM question" + toQuery(filter), (rs, index) -> {
            Question question = new Question();
            question.setId(rs.getString("id"));
            question.setTitle(rs.getString("title"));
            question.setDescription(rs.getString("description"));
            question.setPollId(new ForeignKey(rs.getString("poll_id"), null));
            return question;
        });
    }

    @Override
    public Question save(Question crud) {
        helper.update("INSERT INTO question VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                        "title=?, description=?, poll_id=?",
                crud.getId(),
                crud.getTitle(),
                crud.getDescription(),
                crud.getPollId().getValue(),
                crud.getTitle(),
                crud.getDescription(),
                crud.getPollId().getValue());
        return crud;
    }

    @Override
    public boolean delete(String id) {
        return helper.update("DELETE FROM question WHERE id = ?", id) > 0;
    }
}
