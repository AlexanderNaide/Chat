package ru.gb.Chatterbox.server.service.serverService;

import ru.gb.Chatterbox.server.service.userService.UserService;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.LIST_USERS;

public class ServerForThread extends Server {

    public ServerForThread(UserService userService) {
        super(userService);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server start.");
            userService.start();
            while (!serverSocket.isClosed()) {
                System.out.println("Waiting for connection......");
                Socket socket = serverSocket.accept();
                System.out.println("Client connection.");
                ClientHandler handler = new ClientHandlerOld(socket, this);
                handler.handle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void shutdown() {
        userService.stop();
    }

    public synchronized void removeHandler(ClientHandler handler) {
        this.handlers.remove(handler.getUser());
        sendContacts();
    }

    public synchronized void addHandler(ClientHandler handler){
        this.handlers.put(handler.getUser(), handler);
        sendContacts();
    }

    protected void sendContacts(){

        String contacts = handlers.values().stream()
                .map(ClientHandler::getUser)
                .collect(Collectors.joining(REGEX));

//        String contacts = handlers.stream()
//                .map(ClientHandler::getUser)
//                .collect(Collectors.joining(REGEX));

        String msg = LIST_USERS.getCommand() + REGEX + contacts;

        for (ClientHandler handler : handlers.values()) {
            handler.send(msg);
        }
    }
}
