package ru.gb.Chatterbox.server;

import ru.gb.Chatterbox.server.service.UserService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORT = 6830;

    private List<ClientHandler> handlers;

    private UserService userService;

    public Server(UserService userService) {
        this.userService = userService;
        this.handlers = new ArrayList<>();
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server start.");
            userService.start();
            while(true){
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
        for (ClientHandler handler : handlers){
            handler.send(msg);
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public synchronized boolean isUserAlreadyOnline(String nick){
        for (ClientHandler handler : handlers) {
            if(handler.getUser().equals(nick)){
                return true;
            }
        }
        return false;
    }

    public synchronized void addHandler(ClientHandler handler){
        this.handlers.add(handler);
    }

    public synchronized void removeHandler(ClientHandler handler) {
        this.handlers.remove(handler);
    }

    private void shutdown(){
        userService.stop();
    }
}
