package ru.gb.Chatterbox.server;

import com.sun.scenario.effect.impl.prism.PrImage;
import ru.gb.Chatterbox.server.service.UserService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.*;

public class Server {

    private static final int PORT = 6830;

//    private List<ClientHandler> handlers;
    private final Map<String, ClientHandler> handlers;

    private final UserService userService;

    public Server(UserService userService) {
        this.userService = userService;
        this.handlers = new HashMap<>();
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server start.");
            userService.start();
            while(!serverSocket.isClosed()){
                System.out.println("Waiting for connection......");
                Socket socket = serverSocket.accept();
                System.out.println("Client connection.");
                ClientHandler handler = new ClientHandler(socket, this);
                handler.handle();
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

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

    public synchronized void addHandler(ClientHandler handler){
        this.handlers.put(handler.getUser(), handler);
        sendContacts();
    }

    public synchronized void removeHandler(ClientHandler handler) {
        this.handlers.remove(handler.getUser());
        sendContacts();
    }

    private void shutdown(){
        userService.stop();
    }

    private void sendContacts(){

        String contacts = handlers.values().stream()
                .map(ClientHandler::getUser)
                .collect(Collectors.joining(REGEX));

        System.out.println(contacts);


//        String contacts = handlers.stream()
//                .map(ClientHandler::getUser)
//                .collect(Collectors.joining(REGEX));

        String msg = LIST_USERS.getCommand() + REGEX + contacts;

        for (ClientHandler handler : handlers.values()) {
            handler.send(msg);
        }
    }
}
