package com.example.frontmynbnb.models;

import android.graphics.Bitmap;

public class Message {
    private int id;
    private String text;
    private User sender;
    private User reciever;
    private User hoster;
    private Bitmap userImage = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public User getHoster() {
        return hoster;
    }

    public void setHoster(User hoster) {
        this.hoster = hoster;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public void printMessage(){
        System.out.println("Message: Sender: " + sender.getUsername() + " reciever: " + reciever.getUsername() + " hoster: " + hoster.getUsername() + " text: " + text);
    }
}
