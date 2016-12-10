package ru.disdev.dao;

import ru.disdev.entity.ForeignKey;
import ru.disdev.entity.crud.Question;

import java.util.List;

public class QuestionDAO extends DAO<Question> {

    @Override
    public List<Question> load() {
        return helper.queryForList("SELECT * FROM question", rs -> {
            Question question = new Question();
            question.setId(rs.getString("id"));
            question.setTitle(rs.getString("title"));
            question.setDescription(rs.getString("descption"));
            question.setPollId(new ForeignKey(rs.getString("poll_id"), null));
            return question;
        });
    }

    @Override
    public Question save(Question crud) {
        helper.execute("REPLACE INTO question VALUES(?, ?, ?, ?)",
                crud.getId(),
                crud.getTitle(),
                crud.getDescription(),
                crud.getPollId().getValue());
        return crud;
    }

    @Override
    public boolean delete(String id) {
        return helper.execute("DELETE FROM question WHERE id = ?", id) > 0;
    }
}