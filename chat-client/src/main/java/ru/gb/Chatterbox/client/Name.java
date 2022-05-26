package ru.gb.Chatterbox.client;

public class Name {
    private static int count;

    private String name;
    private final int id;

    public Name(String name){
        this.name = name;
        count++;
        this.id = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
