package ru.gb.Chatterbox.client.view.contactPanel.condition;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class ConditionGroup extends ConditionItem{

    @Override
    public Pane createPane(String title) {
        Pane pane = new Pane();

        Label label = new Label(title);
        label.setStyle(" -fx-font-weight: bold; -fx-font-style: italic;");
        pane.getChildren().add(0, label);

        return pane;
    }
}
