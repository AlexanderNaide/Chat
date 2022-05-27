package ru.gb.Chatterbox.client;

import java.util.ArrayList;
import java.util.List;

public class Groups {
    private final ArrayList<Name> names = new ArrayList<>();
    private String title;
    private boolean unfold;

    public Groups(String title){
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

    public void add(Name name){
        names.add(name);
    }
    public void addGroup(List<Name> nameList){
        names.addAll(nameList);
    }
    public ArrayList<Name> getGroup(){
        return names;
    }
}
