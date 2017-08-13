package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 8/12/17.
 *
 */

public class User implements Parcelable{
    private String name;
    private String providerId;
    private String email;
    private String uid;
    private String profilePicture;
    private boolean allowDisplayingMobileNumber;
    private boolean allowDisplayingEmail;

    public User(String name, String providerId, String email, String uid, String profilePicture) {
        this.name                       = name;
        this.providerId                 = providerId;
        this.email                      = email;
        this.uid                        = uid;
        this.profilePicture             = profilePicture;
        allowDisplayingMobileNumber     = true;
        allowDisplayingEmail            = true;

    }
    protected User(Parcel in) {
        name                            = in.readString();
        providerId                      = in.readString();
        email                           = in.readString();
        uid                             = in.readString();
        profilePicture                  = in.readString();
        allowDisplayingMobileNumber     = in.readByte() != 0;
        allowDisplayingEmail            = in.readByte() != 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(providerId);
        dest.writeString(email);
        dest.writeString(uid);
        dest.writeString(profilePicture);
        dest.writeByte((byte) (allowDisplayingMobileNumber ? 1 : 0));
        dest.writeByte((byte) (allowDisplayingEmail ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isAllowDisplayingMobileNumber() {
        return allowDisplayingMobileNumber;
    }

    public void setAllowDisplayingMobileNumber(boolean allowDisplayingMobileNumber) {
        this.allowDisplayingMobileNumber = allowDisplayingMobileNumber;
    }

    public boolean isAllowDisplayingEmail() {
        return allowDisplayingEmail;
    }

    public void setAllowDisplayingEmail(boolean allowDisplayingEmail) {
        this.allowDisplayingEmail = allowDisplayingEmail;
    }
}
