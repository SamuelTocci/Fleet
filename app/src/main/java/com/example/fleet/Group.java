package com.example.fleet;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.ArrayList;

public class Group implements Parcelable {

    private String id;
    private String name;
    private String description;
    private int userCount;
    private int active;
    private ArrayList<String> users = new ArrayList<>();

    public Group(String id, String name, String description, int userCount){
        this.id = id;
        this.name = name;
        this.description = description;
        this.userCount = userCount;
        active = 0;
    }

    protected Group(Parcel in) {
        description = in.readString();
        id = in.readString();
        name = in.readString();
        userCount = in.readInt();
        users = in.readArrayList(null);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUserCount() {
        return userCount;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(userCount);
        dest.writeInt(active);
        dest.writeList(users);
    }

}