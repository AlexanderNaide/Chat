package ru.gb.Chatterbox.server.service.userService;

import ru.gb.Chatterbox.server.error.WrongCredentialsException;

import java.sql.*;

public class inSQLUserServiceImpl implements UserService {

    private static Connection connection;
    private static Statement statement;

    public inSQLUserServiceImpl() {
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
                ResultSet resultSet = statement.executeQuery(String.format("SELECT EXISTS(SELECT * FROM Chat_Client_BD where nick = '%s')", nick));
                if (resultSet.getInt(1) == 0) {
                    statement.execute(String.format("INSERT INTO Chat_Client_BD (nick, log, pas) VALUES ('%s', '%s', '%s')", nick, log, pas));
                }
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
        connection = DriverManager.getConnection("jdbc:sqlite:chat-server\\SQL_ChatServer.db");
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
        try {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT nick FROM Chat_Client_BD WHERE log = '%s' and pas = '%s'", login, password));
            if (!resultSet.isClosed()) {
                return resultSet.getString(1);
            }
            else{
                throw new WrongCredentialsException("Wrong login or password.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String changeNick(String login, String newNick) {
        try {
            ResultSet resultNick = statement.executeQuery(String.format("SELECT EXISTS(SELECT * FROM Chat_Client_BD where nick = '%s')", newNick));
            if (resultNick.getInt(1) != 0) {
                throw new WrongCredentialsException("This nickname is already taken.");
            }
            statement.execute(String.format("UPDATE Chat_Client_BD SET nick = '%s' WHERE log = '%s'", newNick, login));
            return newNick;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createUser(String login, String password, String newNick) {
        try {
            ResultSet resultNick = statement.executeQuery(String.format("SELECT EXISTS(SELECT * FROM Chat_Client_BD where nick = '%s')", newNick));
            if (resultNick.getInt(1) != 0) {
                throw new WrongCredentialsException("This nickname is already taken.");
            }
            ResultSet resultLog = statement.executeQuery(String.format("SELECT EXISTS(SELECT * FROM Chat_Client_BD where log = '%s')", login));
            if (resultLog.getInt(1) != 0) {
                throw new WrongCredentialsException("This login is already taken.");
            }
            statement.execute(String.format("INSERT INTO Chat_Client_BD (nick, log, pas) VALUES ('%s', '%s', '%s')", newNick, login, password));
            return newNick;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(String login, String password) {
        try {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT nick FROM Chat_Client_BD WHERE log = '%s' and pas = '%s'", login, password));
            if (!resultSet.isClosed()) {
                statement.execute(String.format("DELETE FROM Chat_Client_BD WHERE log = '%s'", login));
            } else {
                throw new WrongCredentialsException("Wrong login or password.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePassword(String login, String password, String newPassword) {
        try {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT nick FROM Chat_Client_BD WHERE log = '%s' and pas = '%s'", login, password));
            if (!resultSet.isClosed()) {
                statement.execute(String.format("UPDATE Chat_Client_BD SET pas = '%s' WHERE log = '%s'", newPassword, login));
            } else {
                throw new WrongCredentialsException("Wrong login or password.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
