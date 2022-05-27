package ru.gb.Chatterbox.server.model;

import java.util.ArrayList;
import java.util.Objects;

public class Group {
    private final ArrayList<User> users = new ArrayList<>();
    private String title;

    public Group() {
    }

    public Group(String title) {
        this.title = title;
    }

    public void addUser(User user){
        users.add(user);
    }

    public void removeUser(User user){
        users.remove(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(users, group.users) && Objects.equals(title, group.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, title);
    }
}
