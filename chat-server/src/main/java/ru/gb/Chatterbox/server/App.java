package ru.gb.Chatterbox.server;

import ru.gb.Chatterbox.server.service.impl.inMemoryUserServiceImpl;
import ru.gb.Chatterbox.server.service.impl.inSQLUserServiceImpl;

public class App {
    public static void main(String[] args) {
//        new Server(new inMemoryUserServiceImpl()).start();
        new Server(new inSQLUserServiceImpl()).start();
    }
}
