package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.UserDAO;
import ru.disdev.entity.crud.User;

import java.util.UUID;

public class UserService implements Service {
    private static UserService ourInstance = new UserService();

    public static UserService getInstance() {
        return ourInstance;
    }

    private UserService() {
    }

    private ObservableList<User> users = FXCollections.observableArrayList();
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void load() {

    }

    @Override
    public void delete(int index) {
        User user = users.remove(index);
        if (user != null) {
            userDAO.delete(user.getId());
        }
    }

    public ObservableList<User> getUsers() {
        users = FXCollections.observableArrayList(userDAO.load());
        return users;
    }

    public void save(User user) {
        if (user.getId() != null) {
            users.remove(user);
        } else {
            user.setId(UUID.randomUUID().toString());
        }
        users.add(userDAO.save(user));
    }
}
