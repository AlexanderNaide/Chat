package ru.gb.Chatterbox.client.model.contactPanel;

import java.util.ArrayList;
import java.util.HashMap;

public class Users {

    private final HashMap<String, User> list = new HashMap<>();

    public Users initialise(){
        list.put("Толик", new User("Толик", true, false));
        list.put("Ваня", new User("Ваня", false, false));
        list.put("Рома", new User("Рома", false, false));
        list.put("Ира", new User("Ира", true, true));
        list.put("Дашка", new User("Дашка", true, true));
        list.put("Женька-печенька", new User("Женька-печенька", true, true));
        list.put("Танюха", new User("Танюха", false, true));
        return this;
    }

    public void addUser(User user){
        list.put(user.getNick(), user);
    }

    public void removeUser(User user){
        list.remove(user.getNick());
    }

    public User get(String str){
        return list.get(str);
    }

    public boolean contains (String str){
        return list.containsKey(str);
    }
}
