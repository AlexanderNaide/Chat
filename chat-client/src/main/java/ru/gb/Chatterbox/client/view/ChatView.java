package ru.gb.Chatterbox.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.gb.Chatterbox.client.lang.Language;
import ru.gb.Chatterbox.client.model.contactPanel.Group;
import ru.gb.Chatterbox.client.model.contactPanel.Groups;
import ru.gb.Chatterbox.client.model.contactPanel.Title;
import ru.gb.Chatterbox.client.model.contactPanel.User;
import ru.gb.Chatterbox.client.view.contactPanel.ContactPanel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatView implements Initializable {
    public ListView<Pane> contactList;
    private ContactPanel contactPanel;
    private Groups groups;
    private Language language;
    @FXML
    private TextArea chatArea;
//    @FXML
    private VBox loginPanel;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactPanel = new ContactPanel(contactList);
        language = new Language(this);

    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public void updateItems(){
        contactPanel.updateItems(groups.getList());
    }

    public ArrayList<Title> getSelectedIndexes(){
        return contactPanel.getSelectedItems(contactList.getSelectionModel().getSelectedIndices());
    }

    public void appendMessage(String text){
        ArrayList <Title> list = getSelectedIndexes();
        StringBuilder forMessage = new StringBuilder();
        if(list.isEmpty()){
            forMessage.append(" ").append("ALL");
        } else {
            for (Title title : list) {
                if (title instanceof User user){
                    String recipient = user.getName();
                    forMessage.append(" ").append(recipient).append(",");
                } else {
                    Group group = (Group) title;
                    String recipient = group.getTitle();
                    forMessage.append(" ").append(recipient).append(",");
                }
            }
            if (!forMessage.isEmpty()) {
                forMessage.deleteCharAt(forMessage.length() - 1);
            }
        }
        text = language.text("Message for") + forMessage + ":\n    " + text;
        appendText(text);
    }

    protected void appendText(String text){
        chatArea.appendText(text + System.lineSeparator());
    }

    protected void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                language.text(error),
                ButtonType.CLOSE
        );
        alert.showAndWait();
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
