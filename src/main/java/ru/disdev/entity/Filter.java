package ru.disdev.entity;

public @interface Filter {
    String fieldName() default "";

    Operator operator() default Operator.LIKE;
}
