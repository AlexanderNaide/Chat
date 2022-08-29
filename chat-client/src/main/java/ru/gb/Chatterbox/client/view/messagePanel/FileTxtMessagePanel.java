package ru.gb.Chatterbox.client.view.messagePanel;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileTxtMessagePanel implements MessagePanel{

    public TextArea chatArea;
    private File fileHistory;
    int historySize = 100;

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

    /**
     * Предварительно подгрузить зависимость в Pom.xml (для подключения ReversedLinesFileReader)
     *         <dependency>
     *             <groupId>commons-io</groupId>
     *             <artifactId>commons-io</artifactId>
     *             <version>2.6</version>
     *         </dependency>
     */
    @Override
    public void readHistory(String name) {
        try{
            fileHistory = new File("chat-client/src/main/resources/history/" + name + ".txt");
            if (!fileHistory.createNewFile()){
                ArrayList<String> history = new ArrayList<>();
                try (ReversedLinesFileReader reader = new ReversedLinesFileReader(fileHistory, StandardCharsets.UTF_8)){
                    String str;
                    while((str = reader.readLine()) != null && history.size() < historySize){
                        history.add(str);
                    }
                    for (int i = history.size() - 1; i >= 0 ; i--) {
                        appendHistory(history.get(i));
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

/*    @Override
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
    }*/

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
