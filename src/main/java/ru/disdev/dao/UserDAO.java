package ru.disdev.dao;

import ru.disdev.entity.Sex;
import ru.disdev.entity.crud.User;

import java.util.List;

public class UserDAO extends DAO<User> {
    @Override
    public List<User> load() {
        return helper.query("SELECT * FROM user", (rs, index) -> {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setFio(rs.getString("fio"));
            user.setSex(Sex.valueOf(rs.getString("sex")));
            return user;
        });
    }

    @Override
    public User save(User crud) {
        helper.update("INSERT INTO user(id, fio, birthday, sex) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE " +
                        "fio=?, birthday=?, sex=?",
                crud.getId(),
                crud.getFio(),
                crud.getBirthday(),
                crud.getSex().name(),
                crud.getFio(),
                crud.getBirthday(),
                crud.getSex().name());
        return crud;
    }

    @Override
    public boolean delete(String id) {
        return helper.update("DELETE FROM user WHERE id = ?", id) > 0;
    }
}
