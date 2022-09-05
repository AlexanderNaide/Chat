package ru.gb.Chatterbox.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.Chatterbox.server.service.serverService.Server;
import ru.gb.Chatterbox.server.service.serverService.ServerForExecutorService;
import ru.gb.Chatterbox.server.service.userService.inSQLUserServiceImpl;

public class App {
    public static final Logger logger = LogManager.getLogger(Server.class);
    public static void main(String[] args) {
//        new Server(new inMemoryUserServiceImpl()).start();
//        new Server(new inSQLUserServiceImpl()).start();
        new ServerForExecutorService(new inSQLUserServiceImpl()).start();
    }
}
