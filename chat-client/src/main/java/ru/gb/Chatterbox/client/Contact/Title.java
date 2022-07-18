package ru.gb.Chatterbox.client.Contact;

import javafx.scene.Node;
import javafx.scene.control.FocusModel;

public class Title extends FocusModel {
    String title = "Что я тут пишу";

    @Override
    protected int getItemCount() {
        return 0;
    }

    @Override
    protected Object getModelItem(int i) {
        return null;
    }
}
