package ru.gb.Chatterbox.client.lang;

import ru.gb.Chatterbox.client.ChatController;
import ru.gb.Chatterbox.client.view.ChatView;

public class Language {
    
    private String lang;
    Translate rus = new rus();
//    ChatController chat;
    ChatView chat;

//    public Language(ChatController chat) {
    public Language(ChatView chat) {
        this.chat = chat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String text(String text) {
        if ("rus".equals(getLang())) {
            return rus.getText(text);
        } else {
            return text;
        }
    }

    public void redrawing(String lang){
        this.lang = lang;
//        chat.contactPanel_____Old.refresh();
//        chat.btnSend.setText(text("SEND"));
//
//        chat.labelRegOnLogin.setText(text("Registration"));
//        chat.labelLoginOnLogin.setText(text("Login:"));
//        chat.labelPasswordOnLogin.setText(text("Password:"));
//        chat.buttonConnectOnLogin.setText(text("Connect"));
//
//        chat.labelAuthOnReg.setText(text("Authorization"));
//        chat.labelLoginOnReg.setText(text("Login:"));
//        chat.labelPasswordOnLReg.setText(text("Password:"));
//        chat.labelNickOnLReg.setText(text("Nickname"));
//        chat.buttonRegOnReg.setText(text("Register"));


    }
}


