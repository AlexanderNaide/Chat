package ru.gb.Chatterbox.client;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.gb.Chatterbox.client.lang.Language;
import ru.gb.Chatterbox.client.net.MessageProcessor;
import ru.gb.Chatterbox.client.net.NetworkService;
import ru.gb.Chatterbox.enums.Command;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
//import java.awt.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.IntBuffer;
import java.util.*;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
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
    public VBox componentContactList;

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

        Dragboard db = primaryStage.getScene().startDragAndDrop(TransferMode.MOVE);

        WritableImage image = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        PixelWriter pw = image.getPixelWriter();
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                pw.setArgb(x, y, bufferedImage.getRGB(x, y));
            }
        }



        db.setDragView(image);

//        ClipboardContent cc = new ClipboardContent();
//        cc.putString("hfhfhfh");
//        db.setContent(cc);

//        setDragView(bufferedImage);

        File file = new File("C:/MyImages/b1.png");
        BufferedImage bi = convert(image);
        ImageIO.write(bi, "png", file);
//        ImageIO.write(bufferedImage, "png", file);

    }

    public BufferedImage convert(Image fxImage) {
        int width = (int) Math.ceil(fxImage.getWidth());
        int height = (int) Math.ceil(fxImage.getHeight());

        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        int[] buffer = new int[width];

        PixelReader reader = fxImage.getPixelReader();
        WritablePixelFormat<IntBuffer> format =
                PixelFormat.getIntArgbInstance();
        for (int y = 0; y < height; y++) {
            reader.getPixels(0, y, width, 1, format, buffer, 0, width);
            image.getRaster().setDataElements(0, y, width, 1, buffer);
        }

        return image;
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
