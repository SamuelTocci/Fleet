package com.example.fleet;

public class User {
    private String first_name;
    private String last_name;

    public User(String first_name, String last_name) {
        this.first_name = first_name; // naam uiteindelijk uit Fb API halen
        this.last_name = last_name;
    }

}