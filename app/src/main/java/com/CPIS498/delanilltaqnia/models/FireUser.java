package com.CPIS498.delanilltaqnia.models;

public class FireUser {
    private String uid;
    private String name;
    private String email;
    private String password;

    public FireUser(String uid, String name, String email, String password) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public FireUser(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
