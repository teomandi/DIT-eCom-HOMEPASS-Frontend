package com.example.frontmynbnb.models;

public class Availability {
    private int id;
    private String from;
    private String to;

    public Availability(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public Availability() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void print(){
        System.out.println(from + " --- " + to);
    }
}
