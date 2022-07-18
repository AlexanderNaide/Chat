package ru.gb.Chatterbox.client.model.contactPanel;

import javafx.scene.layout.Pane;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionItem;

import java.util.ArrayList;

public class User extends Title{

    private final String nick;
    private String name;
    private boolean isOnLine;
    private boolean isNew;

    public User(String nick){
        this.nick = nick;
        this.name = nick;
    }

    public User(String nick, boolean isOnLine, boolean isNew){
        this.nick = nick;
        this.name = nick;
        this.isOnLine = isOnLine;
        this.isNew = isNew;
    }

    @Override
    public Pane visit(ConditionItem conditionItem) {
        return conditionItem.createPane(getName());
    }

    public void setName(String name){
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public String getName() {
        if(name != null) {
            return name;
        } else {
            return nick;
        }
    }

    public void setIsOnLine(boolean onLine) {
        isOnLine = onLine;
    }

    public boolean getIsOnline() {
        return isOnLine;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public synchronized void setNew() {
        Thread isNewUser = new Thread(() ->
                {
                    this.isNew = true;
                    try {
                        Thread.sleep(10000);            //@TODO после всех отладок: тут поменять время на 60000
                    } catch (InterruptedException ignored) {
                    } finally {
                        this.isNew = false;
                    }
                });
        isNewUser.start();
    }
}
