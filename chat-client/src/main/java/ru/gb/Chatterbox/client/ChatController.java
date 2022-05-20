package ru.gb.Chatterbox.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChatController implements Initializable {

    @FXML
    public Button add;

    @FXML
    public Button addGroup;

    @FXML
    public Button del;

    @FXML
    private VBox mainPanel;

    @FXML
    private TextArea chatArea;

    @FXML
    private ListView<String> contacts;

    @FXML
    private TextField inputField;

    @FXML
    private Button btnSend;

    public void mockAction(ActionEvent actionEvent) {
        System.out.println("mock");
    }

    public void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = inputField.getText();
        if(text == null || text.isBlank()){
            return;
        }
        text = "[Message for " + contacts.getFocusModel().getFocusedItem() + ":] " + text;
        chatArea.appendText(text + System.lineSeparator());
        inputField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
/*
        List<String> names = List.of("Vasja", "Masha", "Petja", "Valera", "Sergey");
        contacts.setItems(FXCollections.observableList(names));
        */

        Group all = new Group("Все");
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(all);
        all.addGroup(List.of(new Name("Vasja"), new Name("Masha"), new Name("Petja"), new Name("Valera"), new Name("Sergey")));

        ObservableList<String> list = FXCollections.observableArrayList();

        for (Group g : groups) {
            list.add(g.getTitle());
            if (g.getUnfold()) {
                for (Name n : g.getGroup()) {
                    list.add(n.toString());
                }
            }
        }

        contacts.setItems(list);

        contacts.setOnMouseClicked(e -> {
            for (Group g: groups){
                if (g.getTitle().equals(contacts.getFocusModel().getFocusedItem())){
                    if (g.isUnfold()){
                        g.setUnfold(false);
                        for(Name n: g.getGroup()){
                            list.remove(n.getName());
                        }
                    } else {
                        g.setUnfold(true);
                        for(Name n: g.getGroup()){
                            list.add(n.getName());
                        }
                    }
                }
            }
        });
    }

    public void helpAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader hLoader = new FXMLLoader();
        hLoader.setLocation(this.getClass().getResource("/HelpWindow.fxml"));
        Parent hParent = hLoader.load();
        Scene helpScene = new Scene(hParent);
        Stage helpWindow = new Stage();
        helpWindow.setTitle("Help");
        helpWindow.setScene(helpScene);
        helpWindow.show();

    }
}
