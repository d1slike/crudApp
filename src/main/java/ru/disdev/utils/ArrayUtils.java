package ru.disdev.utils;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collection;

public class ArrayUtils {
    public static double findNearest(double value, Collection<Double> collection) {
        return collection.stream()
                .map(v -> new ImmutablePair<>(Math.abs(value - v), v))
                .sorted((a, b) -> (int) Math.round(a.getLeft() - b.getLeft()))
                .findFirst()
                .orElse(new ImmutablePair<>(0., value))
                .getRight();
    }
}
