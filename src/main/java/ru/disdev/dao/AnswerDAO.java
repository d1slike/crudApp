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
        helper.execute("INSERT INTO answer(id, number, title, question_id) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                        "number=?, title=?, question_id=?",
                crud.getId(),
                crud.getNumber(),
                crud.getTitle(),
                crud.getQuestionId().getValue(),
                crud.getNumber(),
                crud.getTitle(),
                crud.getQuestionId().getValue());
        return crud;
    }

    public List<Answer> getAnswersByIds(String ids) {
        return helper.queryForList("SELECT id, title FROM answer WHERE id IN (?)",
                rs -> {
                    Answer answer = new Answer();
                    answer.setId(rs.getString("id"));
                    answer.setTitle(rs.getString("title"));
                    return answer;
                }, ids);
    }

    @Override
    public boolean delete(String id) {
        return helper.execute("DELETE FROM answer WHERE id = ?", id) > 0;
    }
}
