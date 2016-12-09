package ru.disdev.utils;

import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class FieldValidatorUtils {
    private static final UnaryOperator<TextFormatter.Change> NUMERIC_TEXT_VALIDATOR = change -> {
        String content = change.getControlNewText();
        int length = content.length();
        if (length == 0) {
            return change;
        } else if (length > 12) {
            return null;
        }
        char lastChar = content.charAt(length - 1);
        boolean validLastChar = (lastChar >= '0' && lastChar <= '9') || lastChar == '.';
        boolean dotCheck = content.indexOf('.') == content.lastIndexOf('.');
        return validLastChar && dotCheck ? change : null;
    };


    public static ValidatorBase getRequiredFieldValidator() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Обязательно для заполнения");
        validator.setErrorStyleClass("error");
        return validator;
    }

    public static ValidatorBase getRangeValidator(double min, double max) {
        ValidatorBase validatorBase = new RangeFieldValidator(min, max);
        validatorBase.setMessage("Допустимые значения:[" + min + "..." + max + "]");
        validatorBase.setErrorStyleClass("error");
        return validatorBase;
    }

    public static TextFormatter<Object> getNumericTextFilter() {
        return new TextFormatter<>(NUMERIC_TEXT_VALIDATOR);
    }
}
