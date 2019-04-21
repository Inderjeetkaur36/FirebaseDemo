package com.example.firebasedemo.model;

public class User {
    public String name;
    public String phone;
    public String email;
    public String password;
    public String address;

    public User() {
    }

    public User(String name, String phone, String email, String password, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
    }
}
