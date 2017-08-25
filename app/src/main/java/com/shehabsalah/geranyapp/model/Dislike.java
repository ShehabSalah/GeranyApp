package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 8/19/17.
 * Dislike model
 */

public class Dislike implements Parcelable{
    private String dislikeId;
    private String user_id;
    private String username;
    private String profile_pic;
    private String feedBack;
    private String post_id;

    public Dislike() {
    }

    public Dislike(String user_id, String username, String profile_pic, String dislikeId, String feedBack, String post_id) {
        this.user_id        = user_id;
        this.username       = username;
        this.profile_pic    = profile_pic;
        this.dislikeId      = dislikeId;
        this.feedBack       = feedBack;
        this.post_id        = post_id;
    }

    protected Dislike(Parcel in) {
        user_id             = in.readString();
        username            = in.readString();
        profile_pic         = in.readString();
        dislikeId           = in.readString();
        feedBack            = in.readString();
        post_id             = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(profile_pic);
        dest.writeString(dislikeId);
        dest.writeString(feedBack);
        dest.writeString(post_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dislike> CREATOR = new Creator<Dislike>() {
        @Override
        public Dislike createFromParcel(Parcel in) {
            return new Dislike(in);
        }

        @Override
        public Dislike[] newArray(int size) {
            return new Dislike[size];
        }
    };


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getDislikeId() {
        return dislikeId;
    }

    public void setDislikeId(String dislikeId) {
        this.dislikeId = dislikeId;
    }
}
