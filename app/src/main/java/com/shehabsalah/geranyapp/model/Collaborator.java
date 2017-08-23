package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 8/19/17.
 * Collaborator Model
 */

public class Collaborator implements Parcelable{
    private String collaboratorId;
    private String user_id;
    private String username;
    private String profile_pic;
    private String userEmail;
    private String userNumber;
    private String post_id;
    private boolean isNumberAllowed;
    private boolean isEmailAllowed;


    public Collaborator() {
    }

    public Collaborator(String collaboratorId, String user_id, String username, String profile_pic, String userEmail, String userNumber, String post_id, boolean isEmailAllowed, boolean isNumberAllowed) {
        this.collaboratorId = collaboratorId;
        this.user_id        = user_id;
        this.username       = username;
        this.profile_pic    = profile_pic;
        this.userEmail      = userEmail;
        this.userNumber     = userNumber;
        this.post_id        = post_id;
        this.isEmailAllowed = isEmailAllowed;
        this.isNumberAllowed = isNumberAllowed;
    }

    protected Collaborator(Parcel in) {
        collaboratorId      = in.readString();
        user_id             = in.readString();
        username            = in.readString();
        profile_pic         = in.readString();
        userEmail           = in.readString();
        userNumber          = in.readString();
        post_id             = in.readString();
        isEmailAllowed      = in.readByte() != 0;
        isNumberAllowed     = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(collaboratorId);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(profile_pic);
        dest.writeString(userEmail);
        dest.writeString(userNumber);
        dest.writeString(post_id);
        dest.writeByte((byte) (isEmailAllowed ? 1 : 0));
        dest.writeByte((byte) (isNumberAllowed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Collaborator> CREATOR = new Creator<Collaborator>() {
        @Override
        public Collaborator createFromParcel(Parcel in) {
            return new Collaborator(in);
        }

        @Override
        public Collaborator[] newArray(int size) {
            return new Collaborator[size];
        }
    };

    public String getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorId(String collaboratorId) {
        this.collaboratorId = collaboratorId;
    }

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
