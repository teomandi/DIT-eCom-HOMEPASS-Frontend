package com.example.frontmynbnb.models;

public class User {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean isHost;
    private String createdAt=null;
    private String updatedAt=null;
    private String imageName=null;
    private int id=-1;

    public User(String username,
                String email,
                String password,
                String firstName,
                String lastName,
                String phone,
                boolean isHost,
                String createdAt,
                String updatedAt,
                String imageName,
                int id
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.isHost = isHost;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageName = imageName;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }
}
