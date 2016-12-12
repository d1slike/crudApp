package ru.disdev.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.disdev.dao.LinkDAO;
import ru.disdev.entity.crud.Link;

public class LinkService implements Service {
    private static LinkService ourInstance = new LinkService();

    public static LinkService getInstance() {
        return ourInstance;
    }

    private LinkService() {
    }

    private final ObservableList<Link> links = FXCollections.observableArrayList();
    private final LinkDAO linkDAO = new LinkDAO();

    @Override
    public void load() {
        links.addAll(linkDAO.load());
    }

    public void save(Link link) {
        links.add(link);
        linkDAO.save(link);
    }

    @Override
    public void delete(int index) {
        Link link = links.remove(index);
        if (link != null) {
            linkDAO.delete(link);
        }
    }

    public ObservableList<Link> getLinks() {
        return links;
    }
}
