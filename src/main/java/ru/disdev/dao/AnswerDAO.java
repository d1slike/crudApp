package ru.disdev.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.disdev.entity.ForeignKey;
import ru.disdev.entity.crud.Answer;

import java.util.Collections;
import java.util.List;

public class AnswerDAO extends DAO<Answer> {

    @Override
    public List<Answer> findAll() {
        return helper.query("SELECT * FROM answer", (rs, index) -> {
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
        helper.update("INSERT INTO answer(id, number, title, question_id) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
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

    public List<Answer> getAnswersByIds(List<String> idList) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(helper);
        return template.query("SELECT id, title FROM answer WHERE id IN (:id)",
                Collections.singletonMap("id", idList),
                (rs, index) -> {
                    Answer answer = new Answer();
                    answer.setId(rs.getString("id"));
                    answer.setTitle(rs.getString("title"));
                    return answer;
                });
    }

    @Override
    public boolean delete(String id) {
        return helper.update("DELETE FROM answer WHERE id = ?", id) > 0;
    }
}
