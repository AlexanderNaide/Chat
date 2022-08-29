package ru.gb.Chatterbox.client.view.messagePanel;

import java.io.IOException;

public interface MessagePanel {
    void appendText(String text);
    void readHistory(String name);
}
