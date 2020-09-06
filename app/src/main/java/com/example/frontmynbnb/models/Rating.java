package com.example.frontmynbnb.models;

public class Rating {
    private int id=-1;
    private float degree;
    private String comment;

    public Rating(int degree, String comment) {
        this.degree = degree;
        this.comment = comment;
    }

    public Rating(int id, int degree, String comment) {
        this.id = id;
        this.degree = degree;
        this.comment = comment;
    }

    public Rating(float degree, String comment) {
        this.degree = degree;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

