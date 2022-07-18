package ru.gb.Chatterbox.client.model.contactPanel;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;

public class Groups {

    private final ArrayList<Group> list = new ArrayList<>();

    public Groups initialise(Users users){

        Group all = new Group("Все");
        all.setUnfold(true);
        list.add(0, all);
        all.add(users.get("Толик"));
        all.add(users.get("Ваня"));
        all.add(users.get("Рома"));
        all.add(users.get("Ира"));
        all.add(users.get("Дашка"));
        all.add(users.get("Женька-печенька"));
        all.add(users.get("Танюха"));

        Group myOffice = new Group("Мой отдел");
        list.add(1, myOffice);
        myOffice.add(users.get("Толик"));
        myOffice.add(users.get("Ваня"));
        myOffice.add(users.get("Рома"));
        myOffice.add(users.get("Ира"));

        Group BTK = new Group("БТКашки");
        list.add(2, BTK);
        BTK.add(users.get("Дашка"));
        BTK.add(users.get("Женька-печенька"));
        BTK.add(users.get("Танюха"));

        return this;
    }

    public ArrayList<Group> getList(){
        return list;
    }
}
