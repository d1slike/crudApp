package ru.disdev.components;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.skins.JFXTextFieldSkin;
import javafx.scene.control.TextField;

public class JFXPasswordSkin extends JFXTextFieldSkin {

    public static final char BULLET = '\u2022';

    public JFXPasswordSkin(JFXTextField field) {
        super(field);
    }

    @Override
    protected String maskText(String txt) {
        TextField textField = getSkinnable();

        int n = textField.getLength();
        StringBuilder passwordBuilder = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            passwordBuilder.append(BULLET);
        }

        return passwordBuilder.toString();
    }
}
