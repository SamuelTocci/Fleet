package com.example.fleet;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.ArrayList;

public class Group implements Parcelable {

    private String id;
    private String name;
    private String description;
    private ArrayList<User> users;

    public Group(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    protected Group(Parcel in) {
        description = in.readString();
        id = in.readString();
        name = in.readString();
        users = in.createTypedArrayList(User.CREATOR);
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

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
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
        dest.writeTypedList(users);
    }
}
