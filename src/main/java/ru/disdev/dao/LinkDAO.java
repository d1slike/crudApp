package ru.disdev.dao;

import ru.disdev.entity.ForeignKey;
import ru.disdev.entity.crud.Link;

import java.util.List;

public class LinkDAO extends DAO<Link> {
    @Override
    public List<Link> load() {
        return helper.queryForList("SELECT * FROM link", rs -> {
            Link link = new Link();
            link.setAnswer(new ForeignKey(rs.getString("answer_id"), null));
            link.setQuestion(new ForeignKey(rs.getString("question_id"), null));
            link.setUser(new ForeignKey(rs.getString("user_id"), null));
            return link;
        });
    }

    @Override
    public Link save(Link crud) {
        helper.execute("REPLACE INTO link VALUES(?,?,?)",
                crud.getQuestion().getValue(),
                crud.getAnswer().getValue(),
                crud.getUser().getValue());
        return crud;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    public boolean delete(Link link) {
        return helper.execute("DELETE FROM link WHERE " +
                        "user_id = ? AND " +
                        "question_id = ? AND " +
                        "answer_id = ?", link.getUser().getValue(),
                link.getQuestion().getValue(),
                link.getAnswer().getValue()) > 0;
    }
}
