package ru.gb.Chatterbox.client.lang;

public enum lang {
    ENGLISH("eng"),
    RUSSIAN("rus");

    private String lang;

    lang(String lang) {
        this.lang = lang;
    }

    public String getLanguage() {
        return lang;
    }

    public void setLanguage(String lang) {
        this.lang = lang;
    }
}
