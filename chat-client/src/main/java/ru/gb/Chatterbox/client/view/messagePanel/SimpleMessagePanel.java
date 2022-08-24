package ru.gb.Chatterbox.client.view.messagePanel;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class SimpleMessagePanel implements MessagePanel{

    private VBox componentMessagePanel;
    public TextArea chatArea;

    public SimpleMessagePanel(VBox vb){
        componentMessagePanel = vb;
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.prefHeightProperty().bind(componentMessagePanel.heightProperty());
        componentMessagePanel.getChildren().add(chatArea);
    }

    @Override
    public void appendText(String text) {
        chatArea.appendText(text + System.lineSeparator());
    }

    @Override
    public void readHistory(String name) {
    }
}
