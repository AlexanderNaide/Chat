package ru.gb.Chatterbox.client.view.contactPanel.condition;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

public class ConditionUserOffLine extends ConditionItem{

    @Override
    public Pane createPane(String title) {
        Pane pane = new Pane();

        Circle circle = new Circle();
        circle.setCenterX(10);

        circle.setCenterX(14.0f);
        circle.setCenterY(8.0f);
        circle.setRadius(3.0f);
        circle.setFill(Color.GREY);
        pane.getChildren().add(circle);

        Label label = new Label(title);
        label.setLayoutX(25.0);
        label.setStyle(" -fx-font-style: italic;");
//        label.setStyle(" -fx-font-weight: bold; -fx-font-style: italic;");
        pane.getChildren().add(label);

        return pane;
    }
}
