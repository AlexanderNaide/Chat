package ru.gb.Chatterbox.client.model.contactPanel;

import javafx.scene.layout.Pane;
import ru.gb.Chatterbox.client.model.contactPanel.User;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionGroup;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionItem;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group extends Title{
    private final Map <Integer, User> users = new HashMap<>();
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

    public void add(Integer nom, User name){
        users.put(nom, name);
    }

    public void remove(Integer nom){
        users.remove(nom);
    }

    public Map <Integer, User> getUsers(){
        return users;
    }

    @Override
    public Pane visit(ConditionItem conditionItem) {

        return conditionItem.createPane(title);
    }
}
