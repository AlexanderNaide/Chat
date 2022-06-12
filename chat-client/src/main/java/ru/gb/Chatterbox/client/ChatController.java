package ru.gb.Chatterbox.client;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.gb.Chatterbox.client.lang.Language;
import ru.gb.Chatterbox.client.net.MessageProcessor;
import ru.gb.Chatterbox.client.net.NetworkService;
import ru.gb.Chatterbox.enums.Command;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static javafx.scene.Cursor.*;
import static ru.gb.Chatterbox.client.Application.primaryStage;
import static ru.gb.Chatterbox.client.lang.lang.ENGLISH;
import static ru.gb.Chatterbox.client.lang.lang.RUSSIAN;
import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.*;

public class ChatController implements Initializable, MessageProcessor {

    @FXML
    public TreeView <String> contactPanel;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public VBox registrationPanel;

    @FXML
    public TextField newLoginField;

    @FXML
    public TextField newPasswordField;
    public CheckMenuItem englishSel;
    public CheckMenuItem russianSel;
    public ToggleGroup langAutGroup;
    public ToggleGroup langRegGroup;
    public ToggleButton setAEnglish;
    public ToggleButton setARussian;
    public ToggleButton setREnglish;
    public ToggleButton setRRussian;
    public Label labelRegOnLogin;
    public Label labelLoginOnLogin;
    public Label labelPasswordOnLogin;
    public Button buttonConnectOnLogin;
    public Label labelAuthOnReg;
    public Label labelLoginOnReg;
    public Label labelPasswordOnLReg;
    public Label labelNickOnLReg;
    public Button buttonRegOnReg;
    public Cursor cursor;

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
    public Button btnSend;

    private NetworkService networkService;

    private String user;

    private static Map <String, Group> groups;

    TreeItem <String> root;

    private Language language;

    private double cellSize;

    private ObservableList<TreeItem<String>> movingContacts;

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
            MultipleSelectionModel<TreeItem<String>> selectionModel = contactPanel.getSelectionModel();
            selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
            StringBuilder forMessage = new StringBuilder();
            if(selectionModel.isEmpty()){
                networkService.sendMessage(BROADCAST_MESSAGE.getCommand() + REGEX + text);
                forMessage.append(" ").append("ALL");
            } else {
                for (TreeItem<String> item : selectionModel.getSelectedItems()) {
                    String[] split = item.getValue().split(" ");
                    String recipient = split[1];
                    forMessage.append(" ").append(recipient).append(",");
                    if (groups.containsKey(recipient)) {
                        for (String s : groups.get(recipient).getUsers().keySet()) {
                            networkService.sendMessage(PRIVATE_MESSAGE.getCommand() + REGEX + s + REGEX + text);
                        }
                    } else {
                        networkService.sendMessage(PRIVATE_MESSAGE.getCommand() + REGEX + recipient + REGEX + text);
                    }
                }
                if (!forMessage.isEmpty()) {
                    forMessage.deleteCharAt(forMessage.length() - 1);
                }
            }
            text = language.text("[Message for") + forMessage + ":] " + text;
            chatArea.appendText(text + System.lineSeparator());
            inputField.clear();
        }catch (IOException e){
            showError(language.text("Network error."));
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

        networkService = new NetworkService(this);
        language = new Language(this);
        cellSize = 25.0;
        groups = new HashMap<>();
        Group allUsers = new Group("ALL");
        groups.put(allUsers.getTitle(), allUsers);

        // необязательные группы
        Group myOffice = new Group("Мой отдел");
        myOffice.add(new User("Толик"));
        myOffice.add(new User("Ваня"));
        myOffice.add(new User("Рома"));
        myOffice.add(new User("Ира"));
        groups.put(myOffice.getTitle(), myOffice);
        Group btcOffice = new Group("БТКашки");
        btcOffice.add(new User("Дашка"));
        btcOffice.add(new User("Женька-печенька"));
        btcOffice.add(new User("Танюха"));
        groups.put(btcOffice.getTitle(), btcOffice);

        File usersArchive = new File(String.valueOf(getClass().getResource("users.txt")));
        File groupsArchive = new File(String.valueOf(getClass().getResource("groups.txt")));

        if(usersArchive.length() != 0){
            downloadUsers(usersArchive, allUsers);
        }
        setItems();
    }

    private void setItems() {
        if (contactPanel.getRoot() != null){
        runContact();
        }

        root = new TreeItem<>();
        for (Group g : groups.values()) {
            TreeItem <String> item = new TreeItem<>();
            if (g.getTitle().equals("ALL")  && groups.get("ALL").getUsers().isEmpty()){
                item.setValue("grOff " + g.getTitle());
            } else {
                for (String nick : g.getUsers().keySet()) {
                    if (groups.get("ALL").getUsers().containsKey(nick) && !groups.get("ALL").getUsers().isEmpty()) {
                        item.setValue("grOn " + g.getTitle());
                        break;
                    } else {
                        item.setValue("grOff " + g.getTitle());
                    }
                }
            }
            root.getChildren().add(item);
            item.setExpanded(g.getUnfold());
            for (String s : g.getUsers().keySet()) {
                TreeItem <String> childrenItem;
                if (groups.get("ALL").getUsers().containsKey(s)){
                    childrenItem = new TreeItem<>("usOn " + s);
                } else {
                    childrenItem = new TreeItem<>("usOff " + s);
                }
                item.getChildren().add(childrenItem);
            }
        }
        contactPanel.setFixedCellSize(cellSize);
        contactPanel.setShowRoot(false);
        contactPanel.setRoot(root);
        contactPanel.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        contactPanel.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    String[] row = item.split(" ");
                    switch (row[0]){
                        case "grOn" -> {
                            setText(language.text(row[1]));
                            setStyle(" -fx-font-weight: bold; -fx-font-style: italic;");
                        }
                        case "usOn" -> {
                            setText(language.text(row[1]));
                            setStyle(" -fx-font-style: italic;");
                        }
                        case "usOff" -> {
                            setText(language.text(row[1]));
                            setStyle(" -fx-font-style: italic; -fx-text-fill: Silver;");
                        }
                        default -> {
                            setText(language.text(row[1]));
                            setStyle(" -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
                        }
                    }
                }
            }
        });
    }

    public void runContact(){
        for (TreeItem<String> a : contactPanel.getRoot().getChildren()) {
            Group g = groups.get(a.getValue().substring(a.getValue().indexOf(" ") + 1));
            g.setUnfold(a.expandedProperty().getValue());
        }
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
        contact.remove(0);
        contact.remove(user);
        contact.removeIf(s -> groups.get("ALL").getUsers().containsKey(s));
        groups.get("ALL").addAll(contact);
        setItems();
    }

    private void authOk(String[] split){
        user = split[1];
        loginPanel.setVisible(false);
        primaryStage.setTitle("Chatterbox - " + user);
        primaryStage.setMinHeight(578);
        primaryStage.setMinWidth(662);
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

    public void sendReg(ActionEvent actionEvent) {
        String login = newLoginField.getText();
        String password = newPasswordField.getText();
        String newNick = newNickField.getText();

        if (login.isBlank() || password.isBlank() || newNick.isBlank()){
            return;
        }
        String msg = CREATE_MESSAGE.getCommand() + REGEX + login + REGEX + password + REGEX + newNick;

        try{
            if (!networkService.isConnected()) {
                networkService.connect();
            }
            networkService.sendMessage(msg);
        }catch (IOException e){
            showError("Network error.");
        }
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

    //*************   Изучаемые события   ***************

    public void OnMouseReleased(MouseEvent mouseEvent) {
        if (movingContacts != null){
            double n = mouseEvent.getY();
            if (n > contactPanel.getExpandedItemCount() * cellSize){
                n = (contactPanel.getExpandedItemCount() * cellSize) - 1.0;
            }
            int nom = (int) (n/cellSize);
            String parent;
            if (contactPanel.getTreeItem(nom).getParent().getValue() == null){
                parent = contactPanel.getTreeItem(nom).getValue();
            } else {
                parent = contactPanel.getTreeItem(nom).getParent().getValue();
            }
            parent = parent.substring(parent.indexOf(" ") + 1);
            for (TreeItem<String> item : movingContacts){
                if (item.getParent().getValue() == null){
                    break;
                }
                Group donorG = groups.get(item.getParent().getValue().substring(item.getParent().getValue().indexOf(" ") + 1));
                if (donorG.getTitle().equals(parent)){
                    break;
                }
                User user = groups.get(donorG.getTitle()).getUsers().get(item.getValue().substring(item.getValue().indexOf(" ") + 1));

                groups.get(parent).add(user);
                if (!donorG.getTitle().equals("ALL")){
                    donorG.remove(user);
                }
            }
        movingContacts = null;
        setItems();
        contactPanel.setCursor(DEFAULT);
        }
    }

    public void OnDragDetected(MouseEvent mouseEvent) {
        movingContacts = contactPanel.getSelectionModel().getSelectedItems();
        contactPanel.setCursor(CLOSED_HAND);
    }

    public void OnMouseDragger(MouseEvent mouseEvent) {

        double n = mouseEvent.getY();
        if (n >= contactPanel.getExpandedItemCount() * cellSize){
            n = (contactPanel.getExpandedItemCount() * cellSize) - 1.0;
        }
        int nom = (int) (n/cellSize);
        int nomF = 0;

        for (int i = nom; i > 0; i--) {
            if (contactPanel.getTreeItem(i).getParent().getValue() == null){
                nomF = i;
                break;
            }
        }
        final int[] count = new int[1];

        int finalNomF = nomF;
        contactPanel.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    String[] row = item.split(" ");
                    switch (row[0]){
                        case "grOn" -> {
                            setText(language.text(row[1]));
                            setStyle(" -fx-font-weight: bold; -fx-font-style: italic;");
                            if (finalNomF == count[0]){
                                setStyle("-fx-effect: innershadow(gaussian , #0093ff , 6,0,0,0 ); -fx-font-size: 1.05em; -fx-font-weight: bold; -fx-font-style: italic;");
                            }
                        }
                        case "usOn" -> {
                            setText(language.text(row[1]));
                            setStyle(" -fx-font-style: italic;");
                        }
                        case "usOff" -> {
                            setText(language.text(row[1]));
                            setStyle(" -fx-font-style: italic; -fx-text-fill: Silver;");
                        }
                        default -> {
                            setText(language.text(row[1]));
                            setStyle(" -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
                            if (finalNomF == count[0]){
                                setStyle("-fx-effect: innershadow(gaussian , #0093ff , 6,0,0,0 ); -fx-font-size: 1.05em; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
                            }
                        }
                    }
                    count[0]++;
                }
            }
        });

    }

    public void OnActionDel(ActionEvent actionEvent) {
    }

    public void OnDragDetectedbuttonDel(MouseEvent mouseEvent) {
    }

    public void OnMousePressedDel(MouseEvent mouseEvent) {
    }

}
