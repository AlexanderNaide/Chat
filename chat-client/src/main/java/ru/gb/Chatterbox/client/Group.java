package ru.gb.Chatterbox.client;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final ArrayList<User> users = new ArrayList<>();
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
        users.add(name);
    }

    public ArrayList <User> getUsers(){
        return users;
    }
}
