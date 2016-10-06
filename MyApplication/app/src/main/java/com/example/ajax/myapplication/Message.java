package com.example.ajax.myapplication;

public class Message {
    private String name;
    private String message;
    private String lastSeen;

    public Message(String name, String message, String lastSeen) {
        this.name = name;
        this.message = message;
        this.lastSeen = lastSeen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }
}
