package ru.disdev.utils;

import com.jfoenix.controls.JFXPopup;
import de.jensd.fx.fontawesome.Icon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.concurrent.TimeUnit;

public class PopupUtils {

    private static final Icon WARNING_ICON = new Icon("WARNING");
    private static final Icon INFO_ICON = new Icon("INFO_CIRCLE");

    static {
        Insets insets = new Insets(0, 10, 0, 0);
        WARNING_ICON.setPadding(insets);
        WARNING_ICON.setTextFill(Color.RED);
        INFO_ICON.setPadding(insets);
        INFO_ICON.setTextFill(Color.CYAN);
    }

    public static JFXPopup warningPopup(Pane container, Node source, String message, int secondsToShow) {
        return showPopup(container, source, message, secondsToShow, WARNING_ICON, "");
    }

    public static JFXPopup infoPoup(Pane container, Node source, String message, int secondsToShow) {
        return showPopup(container, source, message, secondsToShow, INFO_ICON, "");
    }

    private static JFXPopup showPopup(Pane container,
                                      Node source,
                                      String message,
                                      int secondsToShow,
                                      Icon icon,
                                      String textStyle) {
        Label label = new Label(message);
        label.setAlignment(Pos.CENTER);
        label.setGraphic(icon);
        label.setPadding(new Insets(20));
        //label.getStylesheets().add(textStyle);
        JFXPopup popup = new JFXPopup(container, label);
        popup.setSource(source);
        popup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.RIGHT);
        DaemonThreadPool.schedule(() -> Platform.runLater(popup::close), secondsToShow, TimeUnit.SECONDS);
        return popup;

    }


}
