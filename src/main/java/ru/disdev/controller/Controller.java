package ru.disdev.controller;

import javafx.fxml.FXML;

public interface Controller {

    @FXML
    void initialize();

    default void acceptData(Object object) {

    }
}
