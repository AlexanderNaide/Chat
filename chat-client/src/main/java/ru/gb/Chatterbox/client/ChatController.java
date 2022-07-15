package ru.gb.Chatterbox.client;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.gb.Chatterbox.client.lang.Language;
import ru.gb.Chatterbox.client.net.MessageProcessor;
import ru.gb.Chatterbox.client.net.NetworkService;
import ru.gb.Chatterbox.enums.Command;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.IntBuffer;
import java.util.*;
import java.util.List;
import static javafx.scene.Cursor.*;
import static ru.gb.Chatterbox.client.Application.primaryStage;
import static ru.gb.Chatterbox.client.lang.lang.ENGLISH;
import static ru.gb.Chatterbox.client.lang.lang.RUSSIAN;
import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.*;

public class ChatController<s> implements Initializable, MessageProcessor {

    public TreeView <String> contactPanel;
    public AnchorPane anchorPane;
    public VBox registrationPanel;
    public TextField newLoginField;
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
    public VBox componentContactList;
    public ImageView dragContact;
    public TextField editing;
    @FXML
    private Button add;
    @FXML
    private Button addGroup;
    @FXML
    private Button del;
    private VBox changeNickPanel;
    private VBox changePasswordPanel;
    @FXML
    private TextField newNickField;
    private TextField newPassField;
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
    private ListView<String> contacts;
    @FXML
    private TextField inputField;
    public Button btnSend;
    private NetworkService networkService;
    private String user;
    private static Map <String, Group> groups;
    private TreeItem <String> root;
    private Language language;
    private double cellSize;
    private ObservableList<TreeItem<String>> movingContacts;
    private Group allUsers;

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
                    String recipient = getStringItem(item.getValue());
                    System.out.println(recipient);
                    forMessage.append(" ").append(recipient).append(",");
                    if (item.getParent().getValue() == null) {
                        for (String s : groups.get(getStringItem(item.getValue())).getUsers().keySet()) {
                            networkService.sendMessage(PRIVATE_MESSAGE.getCommand() + REGEX + s + REGEX + text);
                        }
                    } else {
                        System.out.println(item.getParent().getValue());
                        networkService.sendMessage(PRIVATE_MESSAGE.getCommand() + REGEX + getUser(groups.get(getStringItem(item.getParent().getValue())), recipient).getNick() + REGEX + text);
                    }
                }
                if (!forMessage.isEmpty()) {
                    forMessage.deleteCharAt(forMessage.length() - 1);
                }
            }
            text = language.text("Message for") + forMessage + ":\n    " + text;
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
        allUsers = new Group("ALL");
        allUsers.setUnfold(false);
        groups.put(allUsers.getTitle(), allUsers);
        editing.setMinSize(contactPanel.getMinWidth(), cellSize);
        editing.setMaxSize(contactPanel.getMaxWidth(), cellSize);

        // необязательные группы
        allUsers.add(new User("Толик"));
        allUsers.add(new User("Ваня"));
        allUsers.add(new User("Рома"));
        allUsers.add(new User("Ира"));
        allUsers.add(new User("Дашка"));
        allUsers.add(new User("Женька-печенька"));
        allUsers.add(new User("Танюха"));

        Group myOffice = new Group("Мой отдел");
        myOffice.add(allUsers.getUsers().get("Толик"));
        myOffice.add(allUsers.getUsers().get("Ваня"));
        myOffice.add(allUsers.getUsers().get("Рома"));
        myOffice.add(allUsers.getUsers().get("Ира"));
        groups.put(myOffice.getTitle(), myOffice);

        Group btcOffice = new Group("БТКашки");
        btcOffice.add(allUsers.getUsers().get("Дашка"));
        btcOffice.add(allUsers.getUsers().get("Женька-печенька"));
        btcOffice.add(allUsers.getUsers().get("Танюха"));
        groups.put(btcOffice.getTitle(), btcOffice);

        File usersArchive = new File(String.valueOf(getClass().getResource("users.txt")));
        File groupsArchive = new File(String.valueOf(getClass().getResource("groups.txt")));

        if(usersArchive.length() != 0){
            downloadUsers(usersArchive, allUsers);
        }
        setItems(false);
    }

    private void setItems(boolean is) {
        if (is){
        runContact();
        }

        root = new TreeItem<>();
        for (Group g : groups.values()) {
            TreeItem <String> item = new TreeItem<>();

            for (User user : g.getUsers().values()) {
                if (user.getIsOnline()) {
                    item.setValue("grOn " + g.getTitle());
                    break;
                } else {
                    item.setValue("grOff " + g.getTitle());
                }
            }
            root.getChildren().add(item);
            item.setExpanded(g.getUnfold());
            for (String s : g.getUsers().keySet()) {
                TreeItem <String> childrenItem;
                if (g.getUsers().get(s).getIsOnline()){
                    childrenItem = new TreeItem<>("usOn " + g.getUsers().get(s).getName());
                } else {
                    childrenItem = new TreeItem<>("usOff " + g.getUsers().get(s).getName());
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
                            setText(language.text(getStringItem(item)));
                            setStyle(" -fx-font-weight: bold; -fx-font-style: italic;");
                        }
                        case "usOn" -> {
                            setText(language.text(getStringItem(item)));
                            setStyle(" -fx-font-style: italic;");
                        }
                        case "usOff" -> {
                            setText(language.text(getStringItem(item)));
                            setStyle(" -fx-font-style: italic; -fx-text-fill: Silver;");
                        }
                        default -> {
                            setText(language.text(getStringItem(item)));
                            setStyle(" -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: Silver;");
                        }
                    }
                }
            }
        });
    }

    public void runContact(){
        for (TreeItem<String> a : contactPanel.getRoot().getChildren()) {
            Group g = groups.get(getStringItem(a.getValue()));
            g.setUnfold(a.expandedProperty().getValue());
        }
    }

    private void setItems(String oldTitle, String newTitle) {
        runContact(oldTitle, newTitle);
        setItems(false);
    }

    public void runContact(String oldTitle, String newTitle){
        for (TreeItem<String> a : contactPanel.getRoot().getChildren()) {
            if (getStringItem(a.getValue()).equals(oldTitle)){
                Group g = groups.get(newTitle);
                g.setUnfold(a.expandedProperty().getValue());
            } else {
                Group g = groups.get(getStringItem(a.getValue()));
                g.setUnfold(a.expandedProperty().getValue());
            }
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
        setItems(true);
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

    public User getUser (Group group, String name){
        for (User user : group.getUsers().values()) {
            if (name.equals(user.getName())){
                return user;
            }
        }
        return null;
    }

    //*************   Изучаемые события   ***************

    public void OnMouseReleased(MouseEvent mouseEvent) {
        if (movingContacts != null){
            double n = mouseEvent.getY();
            if (n > contactPanel.getExpandedItemCount() * cellSize){
                n = (contactPanel.getExpandedItemCount() * cellSize) - 1.0;
            }
            int nom = (int) (n/cellSize);
            Group recipientG;
            if (contactPanel.getTreeItem(nom).getParent().getValue() == null){
                recipientG = groups.get(getStringItem(contactPanel.getTreeItem(nom).getValue()));
            } else {
                recipientG = groups.get(getStringItem(contactPanel.getTreeItem(nom).getParent().getValue()));
            }
            for (TreeItem<String> item : movingContacts){
                if (item.getParent().getValue() == null){
                    break;
                }
                Group donorG = groups.get(getStringItem(item.getParent().getValue()));
                if (donorG.equals(recipientG)){
                    break;
                }
                User user = getUser(donorG, getStringItem(item.getValue()));
                recipientG.add(user);
                if (!donorG.getTitle().equals("ALL")){
                    donorG.remove(user);
                }
            }
            setItems(true);
            movingContacts = null;
            dragContact.setVisible(false);
            contactPanel.setCursor(DEFAULT);
        }
    }

    public void OnDragDetected(MouseEvent mouseEvent) throws IOException {
        movingContacts = contactPanel.getSelectionModel().getSelectedItems();
        contactPanel.setCursor(CLOSED_HAND);

        Rectangle2D rectangle2D;
        SnapshotParameters param = new SnapshotParameters();
        ObservableList <Integer> contactsCopyNumber = contactPanel.getSelectionModel().getSelectedIndices();
        BufferedImage bufferedImage = new BufferedImage((int) contactPanel.getWidth() - 2, (int) cellSize * contactsCopyNumber.size(), BufferedImage.SCALE_DEFAULT);

        for (int i = 0; i < contactsCopyNumber.size(); i++) {
            rectangle2D = new Rectangle2D(1, contactsCopyNumber.get(i) * cellSize, contactPanel.getWidth() - 2, cellSize);
            param.setViewport(rectangle2D);
            WritableImage image = contactPanel.snapshot(param, null);
            BufferedImage bi = convert(image);
            bufferedImage.getGraphics().drawImage(bi, 0, i * (int) cellSize, null);
        }


        WritableImage image = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        PixelWriter pw = image.getPixelWriter();
        PixelReader pr = image.getPixelReader();
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                Color color = pr.getColor(x, y);
                Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5);
                pw.setColor(x, y, newColor.brighter());
            }
        }

        dragContact.setFitHeight(image.getHeight());
        dragContact.setFitWidth(image.getWidth());
        dragContact.setImage(image);
        dragContact.setX(mouseEvent.getSceneX() - mouseEvent.getX());
        dragContact.setY(mouseEvent.getSceneY());
        dragContact.setVisible(true);

    }

    public BufferedImage convert(Image image) {
        int width = (int) Math.ceil(image.getWidth());
        int height = (int) Math.ceil(image.getHeight());

        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        int[] buffer = new int[width];

        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<IntBuffer> format =
                PixelFormat.getIntArgbInstance();
        for (int y = 0; y < height; y++) {
            reader.getPixels(0, y, width, 1, format, buffer, 0, width);
            bufferedImage.getRaster().setDataElements(0, y, width, 1, buffer);
        }

        return bufferedImage;
    }

    public void OnMouseDragger(MouseEvent mouseEvent) {

        dragContact.setX(mouseEvent.getSceneX() - mouseEvent.getX());
        dragContact.setY(mouseEvent.getSceneY());

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
                            setText(language.text(getStringItem(item)));
                            setStyle(" -fx-font-weight: bold; -fx-font-style: italic;");
                            if (finalNomF == count[0]){
                                setStyle("-fx-effect: innershadow(gaussian , #0093ff , 6,0,0,0 ); -fx-font-size: 1.05em; -fx-font-weight: bold; -fx-font-style: italic;");
                            }
                        }
                        case "usOn" -> {
                            setText(language.text(getStringItem(item)));
                            setStyle(" -fx-font-style: italic;");
                        }
                        case "usOff" -> {
                            setText(language.text(getStringItem(item)));
                            setStyle(" -fx-font-style: italic; -fx-text-fill: Silver;");
                        }
                        default -> {
                            setText(language.text(getStringItem(item)));
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

    public void onMouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2){
            TreeItem<String> item = contactPanel.getFocusModel().getFocusedItem();
            if (item.getParent().getValue() != null){
                setNewNameContact(item, mouseEvent);
            } else {
                setNewNameGroup(item, mouseEvent);
            }
        }
    }

    private User renameUser;
    private Group renameGroup;
    private InvalidationListener listener;

    private void methodRenameUser(String newName){
        renameUser.setName(newName);
        editing.focusedProperty().removeListener(listener);
        setItems(true);
    }

    private void methodRenameGroup(String newTitle){
        String oldTitle = renameGroup.getTitle();
        renameGroup.setTitle(newTitle);
        groups.put(renameGroup.getTitle(), renameGroup);
        groups.remove(oldTitle);
        editing.focusedProperty().removeListener(listener);
        setItems(oldTitle, newTitle);
    }

    public void setNewNameContact(TreeItem<String> item, MouseEvent mouseEvent){
        renameUser = getUser(groups.get(getStringItem(item.getParent().getValue())), getStringItem(item.getValue()));
        editing.setLayoutX(mouseEvent.getSceneX() - mouseEvent.getX());
        editing.setLayoutY(mouseEvent.getSceneY() - (mouseEvent.getY() % cellSize) + contactPanel.getScaleX());
        editing.setText(renameUser.getName());
        editing.setVisible(true);
        editing.requestFocus();
        listener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (!editing.isFocused() && editing.isVisible()){
                    methodRenameUser(editing.getText());
                    editing.setVisible(false);
                }
            }
        };
        editing.focusedProperty().addListener(listener);
        editing.setOnAction(e -> {
            methodRenameUser(editing.getText());
            editing.setVisible(false);
        });
    }

    public void setNewNameGroup(TreeItem<String> item,  MouseEvent mouseEvent){
        renameGroup = groups.get(getStringItem(item.getValue()));
        editing.setLayoutX(mouseEvent.getSceneX() - mouseEvent.getX());
        editing.setLayoutY(mouseEvent.getSceneY() - (mouseEvent.getY() % cellSize) + contactPanel.getScaleX());
        editing.setText(renameGroup.getTitle());
        editing.setVisible(true);
        editing.requestFocus();
        listener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (!editing.isFocused() && editing.isVisible()){
                    methodRenameGroup(editing.getText());
                    editing.setVisible(false);
                }
            }
        };
        editing.focusedProperty().addListener(listener);
        editing.setOnAction(e -> {
            methodRenameGroup(editing.getText());
            editing.setVisible(false);
        });
    }

    public String getStringItem (String item){
        return item.substring(item.indexOf(" ") + 1);
    }
}
