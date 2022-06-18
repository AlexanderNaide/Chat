package ru.gb.Chatterbox.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group{
    private final Map <String, User> users = new HashMap<>();
    private String title;
    private boolean unfold;

    public Group(String title){
        this.title = title;
        this.unfold = true;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnfold(boolean unfold) {
        this.unfold = unfold;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getUnfold() {
        return unfold;
    }

    public boolean isUnfold() {
        return unfold;
    }

    @Override
    public String toString() {
        return title;
    }

    public void add(User name){
        users.put(name.getNick(), name);
    }

    public void remove(User name){
        users.remove(name.getNick(), name);
    }

    public void addAll(List<String> name){
        for (String s : name) {
            users.put(s, new User(s));
        }
    }

    public Map <String, User> getUsers(){
        return users;
    }
}
