package ru.gb.Chatterbox.client.model.contactPanel;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;

public class Contacts{

    private final HashMap<String, User> list = new HashMap<>();

    public Contacts initialise(){
        list.put("Толик", new User("Толик", true, false));
        list.put("Ваня", new User("Ваня", false, false));
        list.put("Рома", new User("Рома", false, false));
        list.put("Ира", new User("Ира", true, true));
        list.put("Дашка", new User("Дашка", true, true));
        list.put("Женька-печенька", new User("Женька-печенька", true, true));
        list.put("Танюха", new User("Танюха", false, true));
        return this;
    }

    public User getUser(String nick){
        return list.get(nick);
    }

    public void addUser(User user){
        list.put(user.getNick(), user);
    }

    public void removeUser(User user){
        list.remove(user.getNick());
    }

    public HashMap<String, User> getList(){
        return list;
    }
}
