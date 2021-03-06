package com.example.frontmynbnb.models;

public class Rule {
    private int id=-1;
    private String content;

    public Rule(int id, String content) {
        this.id = id;
        this.content = content;
    }
    public Rule(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
