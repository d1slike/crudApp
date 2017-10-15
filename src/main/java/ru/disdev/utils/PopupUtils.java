package ru.disdev.utils;

import com.jfoenix.controls.JFXPopup;
import de.jensd.fx.fontawesome.Icon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
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
        INFO_ICON.setTextFill(Color.BLUE);
    }

    public static JFXPopup warningPopup(Region container, String message, int secondsToShow) {
        return showPopup(container, message, secondsToShow, WARNING_ICON, "");
    }

    public static JFXPopup infoPopup(Region container, String message, int secondsToShow) {
        return showPopup(container, message, secondsToShow, INFO_ICON, "");
    }

    private static JFXPopup showPopup(Region container,
                                      String message,
                                      int secondsToShow,
                                      Icon icon,
                                      String textStyle) {
        Label label = new Label(message);
        label.setAlignment(Pos.CENTER);
        label.setGraphic(icon);
        label.setWrapText(true);
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        label.setPadding(new Insets(20));
        //label.getStylesheets().add(textStyle);
        JFXPopup popup = new JFXPopup(label);
        popup.show(container, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
        DaemonThreadPool.schedule(() -> Platform.runLater(popup::hide), secondsToShow, TimeUnit.SECONDS);
        return popup;

    }


}
