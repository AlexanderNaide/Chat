package ru.gb.Chatterbox.server.service.impl;

import java.sql.*;

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

//            statement.execute("INSERT INTO Chat_Client_BD (id, name, score) VALUES (1, 'Alex', 4)");

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Chat_Client_BD");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("nick") + " " + resultSet.getString("log") + " " + resultSet.getString("pas"));
            }

//            statement.execute("SELECT EXISTS(SELECT * FROM Chat_Client_BD where nick = ?)");
            System.out.println(statement.execute("SELECT EXISTS(SELECT * FROM Chat_Client_BD WHERE nick = 'nick1'"));
//            System.out.println(statement.executeQuery("SELECT * FROM Chat_Client_BD WHERE nick = 'nick10'"));
//            System.out.println(statement.execute(String.format("SELECT EXISTS(SELECT * FROM Chat_Client_BD WHERE nick = %s", "nick1")));


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
        connection = DriverManager.getConnection("jdbc:sqlite:E:\\Java\\Chat\\chat-server\\SQL_ChatServer.db");
        statement = connection.createStatement();
    }

    private static void disconnect() throws SQLException {
        connection.close();
    }
}
