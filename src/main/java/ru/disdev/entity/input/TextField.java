package ru.disdev.entity.input;

import ru.disdev.entity.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TextField {
    String name();
    String description() default "";

    Type type() default Type.DOUBLE;
    boolean isRequired() default true;
}
