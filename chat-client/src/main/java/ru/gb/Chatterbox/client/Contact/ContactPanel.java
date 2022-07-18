package ru.gb.Chatterbox.client.Contact;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import org.w3c.dom.events.EventTarget;

import java.util.ArrayList;
import java.util.List;

public class ContactPanel {

    private final VBox contactPanel;

    public ContactPanel(VBox contactPanel){
        this.contactPanel = contactPanel;
        insertNewContact();
    }

    private void insertNewContact() {

        Label label1 = new Label("ghghghghgh");
        label1.setStyle(" -fx-font-style: italic; -fx-text-fill: Silver;");

        Label label2 = new Label("привет");
        Label label3 = new Label("мамашка");
        label3.setStyle(" -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
        Label label4 = new Label("гироскутер");

        label4.setOnMouseClicked(e -> {
//            System.out.println(e.getSource());
            label4.setStyle("-fx-effect: innershadow(gaussian , #0093ff , 6,0,0,0 ); -fx-font-size: 1.05em; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
        });
        contactPanel.setOnMouseClicked(e ->{
            contactPanel.getChildren().get((int)e.getY() / 26).setStyle("-fx-effect: innershadow(gaussian , #0093ff , 6,0,0,0 ); -fx-font-size: 1.05em; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
            System.out.println(contactPanel.getChildren().get((int)e.getY() / 26));
//            System.out.println(e.getSource());
//            System.out.println(e.getTarget());
        });

        contactPanel.getChildren().add(label1);
        contactPanel.getChildren().add(label2);
        contactPanel.getChildren().add(label3);
        contactPanel.getChildren().add(label4);

        Pane pane1 = new Pane();
        pane1.setLayoutY(26.0);
        Ellipse ellipse = new Ellipse();
        ellipse.setCenterX(10.0f);
        ellipse.setCenterY(13.0f);

        // Radius X
        ellipse.setRadiusX(5.0f);

        // Radius Y
        ellipse.setRadiusY(5.0f);

        // Fill color.
        ellipse.setFill(Color.CORNFLOWERBLUE);

        pane1.getChildren().add(ellipse);

        Label label5 = new Label("Пивасик");
        label5.setLayoutX(20.0);
        label5.setLayoutY(4.0);

        pane1.getChildren().add(label5);


        pane1.setStyle(".tree-cell");
        contactPanel.getChildren().add(pane1);

    }


    public double getMinWidth() {
        return contactPanel.getMinWidth();
    }

    public double getMaxWidth() {
        return contactPanel.getMaxWidth();
    }
}
