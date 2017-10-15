package ru.disdev.dao;

import ru.disdev.entity.ForeignKey;
import ru.disdev.entity.crud.Link;
import ru.disdev.entity.crud.QuestionStatistic;

import java.util.List;

public class LinkDAO extends DAO<Link> {
    @Override
    public List<Link> findAll() {
        return helper.query("SELECT * FROM link", (rs, index) -> {
            Link link = new Link();
            link.setAnswer(new ForeignKey(rs.getString("answer_id"), null));
            link.setQuestion(new ForeignKey(rs.getString("question_id"), null));
            link.setUser(new ForeignKey(rs.getString("user_id"), null));
            return link;
        });
    }

    @Override
    public Link save(Link crud) {
        helper.update("REPLACE INTO link(question_id, answer_id, user_id) VALUES(?,?,?)",
                crud.getQuestion().getValue(),
                crud.getAnswer().getValue(),
                crud.getUser().getValue());
        return crud;
    }

    public Integer getTotalQuestionAnswerCount(String questionId) {
        return helper.queryForObject("SELECT count(*) AS count, " +
                "question_id FROM link " +
                "GROUP BY question_id " +
                "HAVING question_id = ?", (rs, index) -> rs.getInt("count"), questionId);
    }

    public List<QuestionStatistic> getAnswerCount(String questionId, int questionAnswerCount) {
        return helper.query("SELECT answer_id, count(*) AS count " +
                "FROM link " +
                "WHERE question_id = ? " +
                "GROUP BY answer_id", (rs, index) -> {
            QuestionStatistic questionStatistic = new QuestionStatistic();
            questionStatistic.setAnswer(rs.getString("answer_id"));
            questionStatistic.setStatistic((((double) rs.getInt("count") / (double) questionAnswerCount) * 100.) + "%");
            return questionStatistic;
        }, questionId);
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    public boolean delete(Link link) {
        return helper.update("DELETE FROM link WHERE " +
                        "user_id = ? AND " +
                        "question_id = ? AND " +
                        "answer_id = ?", link.getUser().getValue(),
                link.getQuestion().getValue(),
                link.getAnswer().getValue()) > 0;
    }
}
