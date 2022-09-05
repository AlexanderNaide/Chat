package ru.gb.Chatterbox.server;

import ru.gb.Chatterbox.server.service.serverService.ServerForExecutorService;
import ru.gb.Chatterbox.server.service.userService.inSQLUserServiceImpl;

public class App {
    public static void main(String[] args) {
//        new Server(new inMemoryUserServiceImpl()).start();
//        new Server(new inSQLUserServiceImpl()).start();
        new ServerForExecutorService(new inSQLUserServiceImpl()).start();
    }
}
