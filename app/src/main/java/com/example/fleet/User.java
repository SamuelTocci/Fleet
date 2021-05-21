package com.example.fleet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private String id;
    private String first_name;
    private String last_name;
    private String nick_name;
    private Bitmap pfPic;
    private Bundle groupList;

    public User() {
        this.groupList = new Bundle();
    }

    protected User(Parcel in) {
        id = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        nick_name = in.readString();
        groupList = in.readBundle();//TODO checken of classloader nodig is
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

    //Getters
    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public Bitmap getPfPic() {
        return pfPic;
    }

    public void addGroupToBundle(Group group) {
        groupList.putParcelable(group.getId(), group);
    }

    public Bundle getGroupsBundle() {
        return groupList;
    }

    //Setters

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    //functions
    public Bitmap getFacebookProfilePicture() throws IOException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    URL imageURL = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                    pfPic = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return pfPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(nick_name);
        dest.writeBundle(groupList);
    }
}