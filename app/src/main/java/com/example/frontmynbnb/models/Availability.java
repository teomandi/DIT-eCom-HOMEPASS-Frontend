package com.example.frontmynbnb.models;

public class Availability {
    private String from;
    private String to;

    public Availability(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public Availability() { }

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
}
