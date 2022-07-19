package ru.gb.Chatterbox.client.model;

import ru.gb.Chatterbox.client.model.contactPanel.*;
import ru.gb.Chatterbox.client.view.contactPanel.ContactPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.BROADCAST_MESSAGE;
import static ru.gb.Chatterbox.enums.Command.PRIVATE_MESSAGE;

public class ChatModel {

    private Users users;
    private Groups groups;

    public ChatModel(){
        users = new Users().initialise();
        groups = new Groups().initialise(users);
    }

    public Groups getGroups() {
        return groups;
    }

    public String getSendText(String text){
        return BROADCAST_MESSAGE.getCommand() + REGEX + text;
    }

    public String getSendText(String text, String nick){
        return PRIVATE_MESSAGE.getCommand() + REGEX + nick + REGEX + text;
    }

    public void parseUsers(String[] split, String user){
        ArrayList <String> serverContacts = new ArrayList<>(Arrays.asList(split));
        serverContacts.remove(0);
        serverContacts.remove(user);
        users.setAllOnlineStatus(serverContacts);
    }
}
