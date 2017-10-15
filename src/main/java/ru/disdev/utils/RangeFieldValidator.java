package ru.disdev.utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class RangeFieldValidator extends ValidatorBase {

    private final double min;
    private final double max;

    public RangeFieldValidator(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    protected void eval() {
        if (srcControl.get() != null && srcControl.get() instanceof TextInputControl) {
            TextInputControl textField = (TextInputControl) srcControl.get();
            double value = ParseUtils.parseDouble(textField.getText()).orElse(0.0);
            boolean valid = value > min && value <= max;
            hasErrors.setValue(!valid);
        }
    }
}
