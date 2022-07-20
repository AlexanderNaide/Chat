package ru.gb.Chatterbox.client.view.contactPanel;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import ru.gb.Chatterbox.client.model.contactPanel.Group;
import ru.gb.Chatterbox.client.model.contactPanel.Title;
import ru.gb.Chatterbox.client.model.contactPanel.User;
import ru.gb.Chatterbox.client.view.contactPanel.condition.ConditionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactPanel {

    public ListView<Pane> contactList;
    public HashMap <Integer, Title> titleMap;
    ConditionItem.Visitor visitor = new ConditionItem.Visitor();

    private ContactPanel(){}

    public ContactPanel(ListView<Pane> contactList){
        this.contactList = contactList;
        contactList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void updateItems(ArrayList<Group> list) {
        titleMap = new HashMap<>();
        int count = 0;
        for (Group group : list) {
            titleMap.put(count++, group);
            if (group.getUnfold()){
                for (User user : group.getUsers()) {
                    titleMap.put(count++, user);
                }
            }
        }
        contactList.getItems().clear();
        for (Title value : titleMap.values()) {
            contactList.getItems().add(value.visit(visitor));
        }
    }

    public ArrayList<Title> getSelectedItems(List<Integer> list){
        ArrayList<Title> selectedItems = new ArrayList<>();
        for (Integer integer : list) {
            selectedItems.add(titleMap.get(integer));
        }
        return selectedItems;
    }
}
