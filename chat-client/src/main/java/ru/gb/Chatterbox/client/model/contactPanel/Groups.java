package ru.gb.Chatterbox.client.model.contactPanel;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;

public class Groups {

    private final HashMap<Integer, Group> list = new HashMap<>();

    public Groups initialise(HashMap <String, User> contacts){

        Group myOffice = new Group("Мой отдел");
        list.put(1, myOffice);
        myOffice.add(1, contacts.get("Толик"));
        myOffice.add(2, contacts.get("Ваня"));
        myOffice.add(3, contacts.get("Рома"));
        myOffice.add(4, contacts.get("Ира"));

        Group BTK = new Group("БТКашки");
        list.put(2, BTK);
        BTK.add(1, contacts.get("Дашка"));
        BTK.add(2, contacts.get("Женька-печенька"));
        BTK.add(3, contacts.get("Танюха"));

        return this;
    }

    public HashMap<Integer, Group> getList(){
        return list;
    }
}
