package ru.gb.Chatterbox.server.service.serverService;

import ru.gb.Chatterbox.enums.Command;
import ru.gb.Chatterbox.server.error.WrongCredentialsException;

import java.io.*;
import java.net.Socket;

import static ru.gb.Chatterbox.constants.MessageConstants.REGEX;
import static ru.gb.Chatterbox.enums.Command.*;

public class ClientHandlerOld implements ClientHandler{

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Thread handlerThread;
    private Server server;
    private String user;

    public ClientHandlerOld(Socket socket, Server server){
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Handler created.");
            waitingForAuthorization();
        } catch (IOException e){
            System.err.println("Connection problems with user: " + user);
        }
    }

    private void shutdown(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread.currentThread().interrupt();
    }

    /*
    * Работает в параллельном потоке, если пользователь авторизуется за 120 с. цикл прерывается,
    * если же нет - отправляет пользователю сообщение об истекшем тайм-ауте и закрывает текущий
    * поток пользователя. По данной логике хорошо бы еще на стороне клиента тригериться по такому сообщению и
    * делать выход из приложения, но в задании такого небыло, а из итогового приложения я это
    * выпилю (если Вы не против))), тем более, что хочу сделать у клиента кнопку LogIN/LogOUT,
    * и это выбрасывание будет мешать.
    *
    * Но вот по мне будет логичным сделать счетчик неудачных попыток
    * и после, например, 5 неудачных попыток запрет на авторизацию минут на 5. Но для этого,
    * хорошо бы, если бы Вы подсказали, как запилинговаться на конкретную машину в сети.
    * (но я буду еще думать над этим вопросом, пока понимаю только, что делать такой таймаут
    * просто в handlerThread не совсем логично - т.к. новый socket - новый handler.
    * Нужно получить уникальный идентификатор машины или клиентского приложения - это я еще придумаю)
    */
    public void waitingForAuthorization(){
        Thread waiting = new Thread(() -> {
            long timeStart = System.currentTimeMillis();
            while (user == null && System.currentTimeMillis() - timeStart < 12000){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (user == null){
                String response = ERROR_MESSAGE.getCommand() + REGEX + "The connection timeout has expired.";
                send(response);
                shutdown();
            }
        });
        waiting.start();
    }


    public void handle(){
        handlerThread = new Thread(() -> {
            authorize();

            while (!Thread.currentThread().isInterrupted() && socket.isConnected()){
                try{
                    String message = in.readUTF();
                    parseMessage(message);
                } catch (IOException e){
                    Thread.currentThread().interrupt();
                    System.out.println("Connection broken with client: " + user);
                    server.removeHandler(this);
                }
            }
        });
        handlerThread.start();
    }

    private void parseMessage(String message) {

        String[] split = message.split(REGEX);

        Command command = Command.getByCommand(split[0]);

        switch (command){
            case BROADCAST_MESSAGE -> server.broadcast(user, split[1]);
            case PRIVATE_MESSAGE -> server.privateMsg(user, split[1], split[2]);
            default -> System.out.println("Unknown message" + message);
        }
    }

    private void authorize() {
        System.out.println("Authorizing");

        try{
            while (!socket.isClosed()){
                String msg = in.readUTF();
                if (msg.startsWith(AUTH_MESSAGE.getCommand())){
                    String[] parsed = msg.split(REGEX);
                    String response = "";
                    String nickname = null;

                    try{

                        nickname = server.getUserService().authenticate(parsed[1], parsed[2]);
                    } catch (WrongCredentialsException e){
                        response = ERROR_MESSAGE.getCommand() + REGEX + e.getMessage();
                        System.out.println("Wrong credentials: " + parsed[1]);
                    }
                    if(server.isUserAlreadyOnline(nickname)){
                        response = ERROR_MESSAGE.getCommand() + REGEX + "This client already connected.";
                        System.out.println("Already connected.");
                    }

                    if (!response.equals("")){
                        send(response);
                    } else {
                        this.user = nickname;
                        send(AUTH_OK.getCommand() + REGEX + nickname);
                        server.addHandler(this);
                        break;
                    }
                } else if (msg.startsWith(CREATE_MESSAGE.getCommand())){
                    String[] parsed = msg.split(REGEX);
                    String response = "";
                    String nickname = null;

                    try{
                        nickname = server.getUserService().createUser(parsed[1], parsed[2], parsed[3]);
                    } catch (WrongCredentialsException e){
                        response = ERROR_MESSAGE.getCommand() + REGEX + e.getMessage();
                        System.out.println("Wrong credentials: " + parsed[1]);
                    }
/*                    if(server.isUserAlreadyOnline(nickname)){
                        response = ERROR_MESSAGE.getCommand() + REGEX + "This client already connected.";
                        System.out.println("Already connected.");
                    }*/

                    if (!response.equals("")){
                        send(response);
                    } else {
                        this.user = nickname;
                        send(AUTH_OK.getCommand() + REGEX + nickname);
                        server.addHandler(this);
                        break;
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void send(String msg){
        try{
            out.writeUTF(msg);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }
}
