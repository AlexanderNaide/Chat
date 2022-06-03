package ru.gb.Chatterbox.client;

public class User implements target{

    private String nick;
    private String name;
    private boolean isOnLine;
    private boolean isNew;

    public User(String nick, boolean isNew){
        this.nick = nick;
        if (isNew){
            setNew();
        }
    }

    public String toString() {
        if(name != null) {
            return name;
        } else {
            return nick;
        }
    }

    public String getNick() {
        return nick;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(String nick) {
        if(name != null) {
            return name;
        } else {
            return nick;
        }
    }

    public void setIsOnLine(boolean onLine) {
        isOnLine = onLine;
    }

    public boolean getIsOnline() {
        return isOnLine;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public synchronized void setNew() {
        Thread isNewUser = new Thread(() ->
                {
                    this.isNew = true;
                    try {
                        Thread.sleep(10000);            //@TODO после всех отладок: тут поменять время на 60000
                    } catch (InterruptedException ignored) {
                    } finally {
                        this.isNew = false;
                    }
                });
        isNewUser.start();
    }
}
