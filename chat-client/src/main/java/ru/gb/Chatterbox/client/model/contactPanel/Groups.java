package ru.gb.Chatterbox.client.model.contactPanel;

import java.util.ArrayList;

public class Groups {

    private final ArrayList<Group> list = new ArrayList<>();

    public Groups initialise(Users users){

        Group all = new Group("Все");
        all.setUnfold(false);
        users.setAllUsers(all);
        list.add(0, all);
        for (User user : users.getList().values()) {
            all.add(user);
        }

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
