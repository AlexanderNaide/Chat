package ru.gb.Chatterbox.server.service.impl;

import ru.gb.Chatterbox.server.error.WrongCredentialsException;
import ru.gb.Chatterbox.server.model.User;
import ru.gb.Chatterbox.server.service.UserService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class inMemoryUserServiceImpl implements UserService {

    private final List<User> allUsers;

    public inMemoryUserServiceImpl() {
        this.allUsers = new ArrayList<>();
    }

    @Override
    public void start() {
        File usersArchive = new File(String.valueOf(getClass().getResource("users.txt")));

        if(usersArchive.length() == 0){
            allUsers.add(new User("log1", "pass1", "nick1"));
            allUsers.add(new User("log2", "pass2", "nick2"));
            allUsers.add(new User("log3", "pass3", "nick3"));
            allUsers.add(new User("log4", "pass4", "nick4"));
            allUsers.add(new User("log5", "pass5", "nick5"));
        }

        //TODO здесь реализовать загрузку юзеров из файла

        System.out.println("User service started.");
    }

    @Override
    public void stop() {
        System.out.println("User service stopped.");
    }

    @Override
    public String authenticate(String login, String password) {
        for (User user : allUsers) {
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
    public User createUser(String login, String password, String newNick) {

        for (User user : allUsers) {
            if(user.getNick().equals(newNick)){
                break;
            } else {
                User newUser = new User(login, password, newNick);
                allUsers.add(newUser);
                return newUser;
            }
        }
        throw new WrongCredentialsException("this nickname is already taken.");
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
