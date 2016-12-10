package ru.disdev.dao;

import ru.disdev.entity.ForeignKey;
import ru.disdev.entity.crud.Answer;

import java.util.List;

public class AnswerDAO extends DAO<Answer> {

    @Override
    public List<Answer> load() {
        return helper.queryForList("SELECT * FROM answer", rs -> {
            Answer answer = new Answer();
            answer.setId(rs.getString("id"));
            answer.setTitle(rs.getString("title"));
            answer.setNumber(rs.getInt("number"));
            answer.setQuestionId(new ForeignKey(rs.getString("question_id"), null));
            return answer;
        });
    }

    @Override
    public Answer save(Answer crud) {
        helper.execute("REPLACE INTO answer VALUES(?, ?, ?, ?)",
                crud.getId(),
                crud.getNumber(),
                crud.getTitle(),
                crud.getQuestionId().getValue());
        return crud;
    }

    @Override
    public boolean delete(String id) {
        return helper.execute("DELETE FROM answer WHERE id = ?", id) > 0;
    }
}
