package ru.gb.Chatterbox.server.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class test {

    private static Connection connection;
    private static Statement statement;


    public static void main(String[] args) {

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

            statement.execute("SELECT EXISTS(SELECT * FROM authors where id = ?)");

/*            connection.setAutoCommit(false);
            for (int i = 1; i < 6; i++) {
                String nick = String.format("nick%d", i);
                String log = String.format("log%d", i);
                String pas = String.format("pass%d", i);
                statement.execute(String.format("INSERT INTO Chat_Client_BD (nick, log, pas) VALUES ('%s', '%s', '%s')", nick, log, pas));
            }
            connection.setAutoCommit(true);*/







        } catch (SQLException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }




        try {
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:C:\\Java\\Chat\\chat-server\\SQL_ChatServer.db");
        statement = connection.createStatement();
    }

    private static void disconnect() throws SQLException {
        connection.close();
    }
}
