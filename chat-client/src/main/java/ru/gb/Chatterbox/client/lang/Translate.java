package ru.gb.Chatterbox.client.lang;

import java.util.HashMap;
import java.util.Map;

public interface Translate {
    static Map<String, String> text = new HashMap<String, String>();
    public default String getText(String s){
        return text.getOrDefault(s, s);
    }
}
