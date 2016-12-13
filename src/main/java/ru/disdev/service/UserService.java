package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.UserDAO;
import ru.disdev.entity.crud.Link;
import ru.disdev.entity.crud.User;

import java.util.UUID;

public class UserService implements Service {
    private static UserService ourInstance = new UserService();

    public static UserService getInstance() {
        return ourInstance;
    }

    private UserService() {
    }

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void load() {
        users.addAll(userDAO.load());
    }

    @Override
    public void delete(int index) {
        User user = users.remove(index);
        if (user != null) {
            userDAO.delete(user.getId());
            ObservableList<Link> links = LinkService.getInstance().getLinks();
            for (int i = 0, linksSize = links.size(); i < linksSize; i++) {
                Link link = links.get(i);
                if (link.getUser().getValue().equals(user.getId())) {
                    LinkService.getInstance().delete(i);
                }
            }
        }
    }

    public ObservableList<User> getUsers() {
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
