package ru.gb.Chatterbox.client.model.contactPanel;

import javafx.scene.layout.Pane;
import ru.gb.Chatterbox.client.model.contactPanel.User;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionGroup;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group extends Title{
    private ArrayList<User> users = new ArrayList<>();
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

    public void add(User name){
        users.add(name);
    }

    public void remove(User user){
        users.remove(user);
    }

    public ArrayList <User> getUsers(){
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public Pane visit(ConditionItem.Visitor visitor) {
        return visitor.conditionSelect(this);
    }

}
