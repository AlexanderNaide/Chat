package ru.gb.Chatterbox.client.view.messagePanel;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;

public class FileTxtMessagePanel implements MessagePanel{

    public TextArea chatArea;
    private File fileHistory;

    public FileTxtMessagePanel(VBox vb){
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.prefHeightProperty().bind(vb.heightProperty());
        vb.getChildren().add(chatArea);
    }

    @Override
    public void appendText(String text) {
        chatArea.appendText(text + System.lineSeparator());
        writeHistory(text);
    }

    public void appendHistory(String text) {
        chatArea.appendText(text + System.lineSeparator());
    }

    @Override
    public void readHistory(String name) {
        try{
            fileHistory = new File("chat-client/src/main/resources/history/" + name + ".txt");
            if (!fileHistory.createNewFile()){
                try (BufferedReader reader = new BufferedReader(new FileReader(fileHistory))){
                    ArrayList<String> chat = new ArrayList<>();
                    while(reader.ready()){
                        chat.add(reader.readLine());
                    }
                    for (int i = (chat.size() > 100 ? chat.size() - 100 : 0); i < chat.size() ; i++) {
                        appendHistory(chat.get(i));
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeHistory(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileHistory, true))){
            writer.newLine();
            writer.write(text);
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
