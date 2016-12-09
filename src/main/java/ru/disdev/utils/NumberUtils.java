package ru.disdev.utils;

import java.util.Optional;

public class NumberUtils {
    public static Optional<Double> parseDouble(String value) {
        Double val = null;
        try {
            val = Double.parseDouble(value);
        } catch (Exception ignored) {

        }
        return Optional.ofNullable(val);
    }
}
