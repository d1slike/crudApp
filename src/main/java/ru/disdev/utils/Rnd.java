package ru.disdev.utils;

import java.util.List;
import java.util.Random;

public class Rnd {
    private static final Random RANDOM = new Random();

    public static int get(int min, int max) {
        if (min >= max) {
            int temp = max;
            max = min;
            min = temp;
            if (min == max) {
                max++;
            }
        }
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static int get(int max) {
        return RANDOM.nextInt(max);
    }

    public static <E> E get(List<E> list) {
        return list.get(get(list.size()));
    }

    public static <E> E get(E[] array) {
        return array[get(array.length)];
    }
}
