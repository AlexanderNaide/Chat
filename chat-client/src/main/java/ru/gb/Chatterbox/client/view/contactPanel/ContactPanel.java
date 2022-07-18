package ru.gb.Chatterbox.client.view.contactPanel;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import ru.gb.Chatterbox.client.model.contactPanel.Group;
import ru.gb.Chatterbox.client.model.contactPanel.Groups;
import ru.gb.Chatterbox.client.model.contactPanel.Title;
import ru.gb.Chatterbox.client.model.contactPanel.User;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionGroup;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionItem;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionUserOffLine;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionUserOnLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactPanel {

    private ListView <Pane> contactList;
    private HashMap <Integer, Title> titleMap;
    ConditionItem.Visitor visitor = new ConditionItem.Visitor();

    private ContactPanel(){}

    public ContactPanel(ListView <Pane> contactList){
        this.contactList = contactList;
        contactList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void updateItems(ArrayList<Group> list) {
        titleMap = new HashMap<>();
        int count = 0;
        for (Group group : list) {
            titleMap.put(count++, group);
            if (group.getUnfold()){
                for (User user : group.getUsers()) {
                    titleMap.put(count++, user);
                }
            }
        }

        for (Title value : titleMap.values()) {
            contactList.getItems().add(value.visit(visitor));
        }
    }

    public ArrayList<Title> getSelectedItems(List<Integer> list){
        ArrayList<Title> selectedItems = new ArrayList<>();
        for (Integer integer : list) {
            selectedItems.add(titleMap.get(integer));
        }
        return selectedItems;
    }



//        ListView<Pane> listView = new ListView<>();
//        contactPanel.getChildren().add(listView);


//        Label label1 = new Label("ghghghghgh");
//        label1.setStyle(" -fx-font-style: italic; -fx-text-fill: Silver;");
//
//        Label label2 = new Label("привет");
//        Label label3 = new Label("мамашка");
//        label3.setStyle(" -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
//        Label label4 = new Label("гироскутер");
//
//        label4.setOnMouseClicked(e -> {
//            System.out.println(e.getSource());
//            label4.setStyle("-fx-effect: innershadow(gaussian , #0093ff , 6,0,0,0 ); -fx-font-size: 1.05em; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
//        });
//        contactList.setOnMouseClicked(e ->{
//            contactPanel.getChildren().get((int)(e.getY() / 25)).setStyle("-fx-effect: innershadow(gaussian , #0093ff , 6,0,0,0 ); -fx-font-size: 1.05em; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
//            System.out.println(contactPanel.getChildren().get((int)(e.getY() / 25)));
//            System.out.println(e.getSource());
//            System.out.println(e.getTarget());
//        });

//        contactPanel.getChildren().add(label1);
//        contactPanel.getChildren().add(label2);
//        contactPanel.getChildren().add(label3);
//        contactPanel.getChildren().add(label4);
//
//        Pane pane1 = new Pane();
//
//        pane1.setLayoutY(26.0);
//        Ellipse ellipse = new Ellipse();
//        ellipse.setCenterX(10.0f);
//        ellipse.setCenterY(13.0f);
//
//        // Radius X
//        ellipse.setRadiusX(5.0f);
//
//        // Radius Y
//        ellipse.setRadiusY(5.0f);
//
//        // Fill color.
//        ellipse.setFill(Color.CORNFLOWERBLUE);
//
//        pane1.getChildren().add(ellipse);
//
//        Label label5 = new Label("Пивасик");
//        label5.setLayoutX(20.0);
//        label5.setLayoutY(4.0);
//
//        pane1.getChildren().add(label5);
////        pane1.focusedProperty();
//        pane1.requestFocus();
////        pane1.focusTraversableProperty();
//
//        pane1.getStyleClass().addAll("my-label");
//
//        listView.getItems().add(pane1);
//
//        Pane pane2 = new Pane();
//        Pane pane3 = new Pane();
//        Pane pane4 = new Pane();
//        pane2.getChildren().add(label1);
//        pane3.getChildren().add(label2);
//        pane4.getChildren().add(label3);
//
//        listView.getItems().add(pane2);
//        listView.getItems().add(pane3);
//        listView.getItems().add(pane4);
//
//
//
////        contactPanel.getChildren().add(pane1);
//
//        contactList.requestFocus();

//    }

}
