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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.gb.Chatterbox.client.net.MessageProcessor;
import ru.gb.Chatterbox.client.net.NetworkService;
import ru.gb.Chatterbox.enums.Command;

import javax.swing.event.ChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.AUTH_MESSAGE;
import static ru.gb.Chatterbox.enums.Command.BROADCAST_MESSAGE;

public class ChatController implements Initializable, MessageProcessor {

    @FXML
    private Button add;

    @FXML
    private Button addGroup;

    @FXML
    private Button del;

    @FXML
    private VBox changeNickPanel;

    @FXML
    private VBox changePasswordPanel;

    @FXML
    private TextField newNickField;

    @FXML
    private TextField newPassField;

    @FXML
    private TextField oldPassField;

    @FXML
    private VBox loginPanel;

    @FXML
    private TextField PasswordField;

    @FXML
    private TextField LoginField;

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

    private NetworkService networkService;

    private String user;

    private static ArrayList<Group> groups;

    private static ObservableList <String> list;

    public void mockAction(ActionEvent actionEvent) {
        System.out.println("mock");
    }

    public void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void sendMessage(ActionEvent actionEvent) {
        try{
            String text = inputField.getText();
            if (text == null || text.isBlank()) {
                return;
            }
            networkService.sendMessage(BROADCAST_MESSAGE.getCommand() + REGEX + text); //тут заменить
            text = "[Message for " + contacts.getFocusModel().getFocusedItem() + ":] " + text;
            String recipient = contacts.getFocusModel().getFocusedItem();

/*            if (recipient.equals("ALL")) {
                networkService.sendMessage(BROADCAST_MESSAGE.getCommand() + REGEX + text);
            }*/

            //@TODO private msgs

            chatArea.appendText(text + System.lineSeparator());
            inputField.clear();
        }catch (IOException e){
            showError("Network error.");
        }
    }

    private void showError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                s,
                ButtonType.CLOSE
                );
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
/*
        List<String> names = List.of("Vasja", "Masha", "Petja", "Valera", "Sergey");
        contacts.setItems(FXCollections.observableList(names));
        */
        groups = new ArrayList<>();
        Group allUsers = new Group("Все");
        groups.add(allUsers);

        File usersArchive = new File(String.valueOf(getClass().getResource("users.txt")));
        File groupsArchive = new File(String.valueOf(getClass().getResource("groups.txt")));

        if(usersArchive.length() != 0){
            downloadUsers(usersArchive, allUsers);
        }

        networkService = new NetworkService(this);

        list = FXCollections.observableArrayList();

        for (Group g : groups) {
            list.add(g.getTitle());
            if (g.getUnfold()) {
                for (User user : g.getUsers()) {
                    if (!this.user.equals(user.getNick())) {
                        list.add(user.getName(user.getNick()));
                    }
                }
            }
        }

        contacts.setItems(list);

/*        contacts.setOnMouseClicked(e -> {

            if(mouseEvent.getClickCount() == 2){
                System.out.println("Double clicked");
            }

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
        });*/
    }

    private void downloadUsers(File usersArchive, Group allUsers) {

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

    @Override
    public void processMessage(String message) {

        Platform.runLater(() -> parseMessage(message));
    }
    private void parseMessage(String message){

        String[] split = message.split(REGEX);
        Command command = Command.getByCommand(split[0]);

        switch (command){
            case AUTH_OK -> authOk(split);
            case ERROR_MESSAGE -> showError(split[1]);
            case LIST_USERS -> parseUsers(split);
            default -> chatArea.appendText(split[1] + System.lineSeparator());
        }
    }

    private void parseUsers(String[] split){
        List<String> contact = new ArrayList<>(Arrays.asList(split));
//        contact.set(0, "ALL");

        contact.remove(0);
        contact.remove(user);
        contact.removeIf(s -> list.contains(s));

        list.addAll(contact);
        contacts.setItems(FXCollections.observableList(list));
    }

    private void authOk(String[] split){
        user = split[1];
        loginPanel.setVisible(false);
        Application.primaryStage.setTitle("Chatterbox - " + user);
        mainPanel.setVisible(true);
    }

    public void sendChangeNick(ActionEvent actionEvent) {
        //@TODO
    }

    public void returnToChat(ActionEvent actionEvent) {
        //@TODO
    }

    public void sendChangePass(ActionEvent actionEvent) {
        //@TODO
    }

    public void sendAuth(ActionEvent actionEvent) {
        String login = LoginField.getText();
        String password = PasswordField.getText();

        if (login.isBlank() || password.isBlank()){
            return;
        }
        String msg = AUTH_MESSAGE.getCommand() + REGEX + login + REGEX + password;

        try{
            if (!networkService.isConnected()) {
                networkService.connect();

            }
            networkService.sendMessage(msg);
        }catch (IOException e){
            showError("Network error.");
        }
    }
}
