package ru.gb.Chatterbox.client;

import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpWindow implements Initializable {

    public TextArea helpArea;
    public VBox mainPanel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try (BufferedReader reader = new BufferedReader(new FileReader("./chat-client/src/main/resources/help.txt"))){

            while (reader.ready()){
                helpArea.appendText(reader.readLine() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
