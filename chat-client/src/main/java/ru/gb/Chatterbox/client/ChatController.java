package ru.gb.Chatterbox.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private VBox mainPanel;

    @FXML
    private TextArea chatArea;

    @FXML
    private ListView contacts;

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
        List<String> names = List.of("Vasja", "Masha", "Petja", "Valera", "Sergey");
        contacts.setItems(FXCollections.observableList(names));
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
