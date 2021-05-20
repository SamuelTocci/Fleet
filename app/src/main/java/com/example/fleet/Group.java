package com.example.fleet;

import java.text.DateFormat;
import java.util.ArrayList;

public class Group {

    private String description;
    private String id;
    private String name;

    private ArrayList<User> users;

    public Group(String description, String id, String name, DateFormat tmeeting, Float xmeeting, Float ymeeting, ArrayList<User> users){
        this.description = description;
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
