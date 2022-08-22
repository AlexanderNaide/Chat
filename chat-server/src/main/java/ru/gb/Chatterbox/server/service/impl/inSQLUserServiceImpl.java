package ru.gb.Chatterbox.server.service.impl;

import ru.gb.Chatterbox.server.error.WrongCredentialsException;
import ru.gb.Chatterbox.server.model.User;
import ru.gb.Chatterbox.server.service.UserService;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class inSQLUserServiceImpl implements UserService {

    private static Connection connection;
    private static Statement statement;


    private final Map<String, User> allUsers;

    public inSQLUserServiceImpl() {
        this.allUsers = new HashMap<>();
    }

    @Override
    public void start() {

        try {
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }


        try {
            statement.execute("CREATE TABLE IF NOT EXISTS Chat_Client_BD (" +
                        "nick STRING PRIMARY KEY NOT NULL UNIQUE," +
                        "log STRING NOT NULL UNIQUE," +
                        "pas STRING NOT NULL)");

            connection.setAutoCommit(false);
            for (int i = 1; i < 6; i++) {
                String nick = String.format("nick%d", i);
                String log = String.format("log%d", i);
                String pas = String.format("pass%d", i);
                statement.execute(String.format("INSERT INTO Chat_Client_BD (nick, log, pas) VALUES ('%s', '%s', '%s')", nick, log, pas));
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }

        System.out.println("User service started.");
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
//        connection = DriverManager.getConnection("jdbc:sqlite:C:\\Java\\Chat\\chat-server\\SQL_ChatServer.db");
        connection = DriverManager.getConnection("jdbc:sqlite:E:\\Java\\Chat\\chat-server\\SQL_ChatServer.db");
        statement = connection.createStatement();
    }

    private static void disconnect() throws SQLException {
        connection.close();
    }

    @Override
    public void stop() {
        try {
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        System.out.println("User service stopped.");
    }

    @Override
    public String authenticate(String login, String password) {
        for (User user : allUsers.values()) {
            if (Objects.equals(login, user.getLogin()) && Objects.equals(password, user.getPassword())){
                return user.getNick();
            }
        }
        throw new WrongCredentialsException("Wrong login or password.");
    }

    @Override
    public String changeNick(String login, String newNick) {
        return null; //@TODO
    }

    @Override
    public String createUser(String login, String password, String newNick) {

        if(!allUsers.containsKey(newNick)){
            User newUser = new User(login, password, newNick);
            allUsers.put(newNick, newUser);
            return newNick;
        } else {
            throw new WrongCredentialsException("This nickname is already taken.");
        }
    }

    @Override
    public void deleteUser(String login, String password) {
                     //@TODO
    }

    @Override
    public void changePassword(String login, String password, String newPassword) {
                     //@TODO
    }
}
