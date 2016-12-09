package ru.disdev.entity.input.conditional;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class ElementsList {
    private List<Node> enable = new ArrayList<>();
    private List<Node> disable = new ArrayList<>();

    public List<Node> getEnable() {
        return enable;
    }

    public List<Node> getDisable() {
        return disable;
    }

    public void addToEnable(Node node) {
        enable.add(node);
    }

    public void addToDisable(Node node) {
        disable.add(node);
    }

    public void clear() {
        enable.clear();
        disable.clear();
    }
}
