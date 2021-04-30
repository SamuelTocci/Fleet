package com.example.fleet;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

public abstract class Event implements Parcelable {

    private String id;
    private String group_name;
    private String description;
    private Time meet_time;

    public Event(String id, String group_name, String description) {
        this.id = id;
        this.group_name = group_name;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Time getMeet_time() {
        return meet_time;
    }

    public void setMeet_time(Time meet_time) {
        this.meet_time = meet_time;
    }
}
