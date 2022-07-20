package ru.gb.Chatterbox.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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

import static ru.gb.Chatterbox.client.Application.primaryStage;
import static ru.gb.Chatterbox.client.lang.lang.ENGLISH;
import static ru.gb.Chatterbox.client.lang.lang.RUSSIAN;

public class ChatView {

    public ListView<Pane> contactList;
    public ContactPanel contactPanel;
    public Groups groups;
    public Language language;
    public VBox registrationPanel;
    public ToggleGroup langAutGroup;
    public ToggleGroup langRegGroup;
    public ToggleButton setAEnglish;
    public ToggleButton setARussian;
    public ToggleButton setREnglish;
    public ToggleButton setRRussian;
    public CheckMenuItem englishSel;
    public CheckMenuItem russianSel;
    @FXML
    public TextArea chatArea;
    @FXML
    public VBox loginPanel;
    @FXML
    public VBox mainPanel;

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

    public void appendText(String text){
        chatArea.appendText(text + System.lineSeparator());
    }

    public void showError(String error) {
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

    public void authOk(String user){
        loginPanel.setVisible(false);
        primaryStage.setTitle("Chatterbox - " + user);
        primaryStage.setMinHeight(578);
        primaryStage.setMinWidth(662);
        mainPanel.setVisible(true);
    }

    public void sendAuthorisationWindow(MouseEvent mouseEvent) {
        registrationPanel.setVisible(false);
        primaryStage.setTitle("Chatterbox");
        loginPanel.setVisible(true);
    }

    public void sendRegistrationWindow(MouseEvent mouseEvent) {
        loginPanel.setVisible(false);
        primaryStage.setTitle("Chatterbox");
        registrationPanel.setVisible(true);
    }

    public void selectEnglish(ActionEvent actionEvent) {
        langAutGroup.selectToggle(setAEnglish);
        langRegGroup.selectToggle(setREnglish);
        language.redrawing(ENGLISH.getLanguage());
        englishSel.setSelected(true);
        russianSel.setSelected(false);
    }

    public void selectRussian(ActionEvent actionEvent) {
        langAutGroup.selectToggle(setARussian);
        langRegGroup.selectToggle(setRRussian);
        language.redrawing(RUSSIAN.getLanguage());
        russianSel.setSelected(true);
        englishSel.setSelected(false);
    }
}
