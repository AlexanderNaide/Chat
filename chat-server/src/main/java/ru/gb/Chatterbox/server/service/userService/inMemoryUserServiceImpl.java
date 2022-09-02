package ru.gb.Chatterbox.server.service.userService;

import ru.gb.Chatterbox.server.error.WrongCredentialsException;
import ru.gb.Chatterbox.server.model.User;

import java.io.File;
import java.util.*;

public class inMemoryUserServiceImpl implements UserService {

//    private final List<User> allUsers;
    private final Map<String, User> allUsers;

    public inMemoryUserServiceImpl() {
        this.allUsers = new HashMap<>();
    }

    @Override
    public void start() {
        File usersArchive = new File(String.valueOf(getClass().getResource("users.txt")));

        if(usersArchive.length() == 0){

            allUsers.put("nick1", new User("log1", "pass1", "nick1"));
            allUsers.put("nick2", new User("log2", "pass2", "nick2"));
            allUsers.put("nick3", new User("log3", "pass3", "nick3"));
            allUsers.put("nick4", new User("log4", "pass4", "nick4"));
            allUsers.put("nick5", new User("log5", "pass5", "nick5"));

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
