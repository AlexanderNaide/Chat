package ru.gb.Chatterbox.client;

import javafx.util.Callback;

public class itemString implements Callback {
    private String text;
    private String style;

    public itemString(String text, String style){
        this.text = text;
        this.style = style;
    }

    public String getText() {
        return text;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public Object call(Object param) {
        return text;
    }
}
