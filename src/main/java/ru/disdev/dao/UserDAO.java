package ru.disdev.dao;

import ru.disdev.entity.Sex;
import ru.disdev.entity.crud.User;

import java.util.List;

public class UserDAO extends DAO<User> {
    @Override
    public List<User> load() {
        return helper.queryForList("SELECT * FROM user", rs -> {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setFio(rs.getString("fio"));
            user.setSex(Sex.J.toString().equals(rs.getString("sex")) ? Sex.J : Sex.M);
            return user;
        });
    }

    @Override
    public User save(User crud) {
        helper.execute("REPLACE INTO user VALUES(?,?,?,?)",
                crud.getId(),
                crud.getFio(),
                crud.getBirthday(),
                crud.getSex().toString());
        return crud;
    }

    @Override
    public boolean delete(String id) {
        return helper.execute("DELETE FROM user WHERE id = ?", id) > 0;
    }
}
