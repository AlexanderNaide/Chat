package ru.gb.Chatterbox.client.view.contactPanel.condition;

import javafx.scene.layout.Pane;
import ru.gb.Chatterbox.client.model.contactPanel.Group;
import ru.gb.Chatterbox.client.model.contactPanel.User;

public abstract class ConditionItem {

    public static class Visitor{
        ConditionGroup conditionGroup = new ConditionGroup();
        ConditionUserOnLine conditionUserOnLine = new ConditionUserOnLine();
        ConditionUserOffLine conditionUserOffLine = new ConditionUserOffLine();

        public Pane conditionSelect (Group group){
            String title = group.getTitle();
            return conditionGroup.createPane(title);
        }
        public Pane conditionSelect (User user){
            String title = user.getName();
            if (user.getIsOnline()){
                return conditionUserOnLine.createPane(title);
            } else {
                return conditionUserOffLine.createPane(title);
            }
        }
    }


    public abstract Pane createPane(String title);

}
