package ru.gb.Chatterbox.client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ru.gb.Chatterbox.client.net.NetworkService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.AUTH_MESSAGE;

public class Authorization implements Initializable {


    public VBox loginPanel;
    public TextField LoginField;
    public TextField PasswordField;
    public VBox registrationPanel;
    public TextField newLoginField;
    public TextField newPasswordField;
    public TextField newNickField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void sendRegistrationWindow(MouseEvent mouseEvent) {
        loginPanel.setVisible(false);
        registrationPanel.setVisible(true);
    }

    public void sendAuthorisationWindow(MouseEvent mouseEvent) {
        registrationPanel.setVisible(false);
        loginPanel.setVisible(true);
    }

    public void sendAuth(ActionEvent actionEvent) {
    }

    public void sendReg(ActionEvent actionEvent) {
    }
}