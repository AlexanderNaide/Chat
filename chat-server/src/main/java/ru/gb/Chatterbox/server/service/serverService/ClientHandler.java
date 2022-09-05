package ru.gb.Chatterbox.server.service.serverService;

public interface ClientHandler {

    void handle();
    String getUser();
    void send(String msg);
}
