package ru.gb.Chatterbox.server.service.serverService;

import ru.gb.Chatterbox.server.service.userService.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.*;

public abstract class Server {

    protected static final int PORT = 6830;
    protected final Map<String, ClientHandler> handlers;
    protected final UserService userService;

    public Server(UserService userService) {
        this.userService = userService;
        this.handlers = new HashMap<>();
    }

    public abstract void start();

    public void broadcast(String from, String message){
        String msg = String.format("[%s]: %s", from, message);
        for (ClientHandler handler : handlers.values()){
            if(!handler.getUser().equals(from)){
                handler.send(BROADCAST_MESSAGE.getCommand() + REGEX + msg);
            }
        }
    }

    public void privateMsg(String from, String forUser, String message){
        String msg = String.format("[%s]: %s", from, message);
        if(handlers.containsKey(forUser)){
            handlers.get(forUser).send(PRIVATE_MESSAGE.getCommand() + REGEX + msg);
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public synchronized boolean isUserAlreadyOnline(String nick){
        return handlers.containsKey(nick);
    }

    protected abstract void addHandler(ClientHandler handler);

    protected abstract void removeHandler(ClientHandler handler);

    protected abstract void sendContacts();
}
