package ru.disdev.entity;

public class ForeignKey {

    private final String value;
    private final String label;

    public ForeignKey(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return label == null ? value : label;
    }

    public String getValue() {
        return value;
    }
}
