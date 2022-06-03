package ru.gb.Chatterbox.server.service;

import ru.gb.Chatterbox.server.model.User;

public interface UserService {
    void start();
    void stop();
    String authenticate(String login, String password);
    String changeNick(String login, String newNick);
    String createUser(String login, String password, String newNick);
    void deleteUser(String login, String password);
    void changePassword(String login, String password, String newPassword);
}
