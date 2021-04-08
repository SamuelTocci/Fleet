package com.example.fleet;

import android.media.Image;

public class User {
    private String first_name;
    private String last_name;
    private String nick_name;
    private Image pfPic;

    public User(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

}