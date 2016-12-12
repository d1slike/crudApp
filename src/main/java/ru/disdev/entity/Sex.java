package ru.disdev.entity;

public enum Sex {
    M("М"), J("Ж");

    private final String description;

    Sex(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
