package com.example.frontmynbnb.models;

public class Message {
    private int id;
    private String body;

    public Message(int id, String body) {
        this.id = id;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }
}
