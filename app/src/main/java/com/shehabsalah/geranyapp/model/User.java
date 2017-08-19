package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 8/12/17.
 * User model
 */

public class User implements Parcelable{
    private String providerId;
    private String profileUid;
    private String profileDisplayName;
    private String profileEmail;
    private String profilePhotoUrl;
    private String phoneNumber;
    private boolean allowDisplayingMobileNumber;
    private boolean allowDisplayingEmail;

    public User() {
    }

    public User(String name, String providerId, String email, String uid, String profilePhotoUrl, String phoneNumber) {
        this.profileDisplayName         = name;
        this.providerId                 = providerId;
        this.profileEmail               = email;
        this.profileUid                 = uid;
        this.profilePhotoUrl            = profilePhotoUrl;
        this.phoneNumber                = phoneNumber;
        allowDisplayingMobileNumber     = false;
        allowDisplayingEmail            = true;

    }
    protected User(Parcel in) {
        profileDisplayName              = in.readString();
        providerId                      = in.readString();
        profileEmail                    = in.readString();
        profileUid                      = in.readString();
        profilePhotoUrl                 = in.readString();
        phoneNumber                     = in.readString();
        allowDisplayingMobileNumber     = in.readByte() != 0;
        allowDisplayingEmail            = in.readByte() != 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileDisplayName);
        dest.writeString(providerId);
        dest.writeString(profileEmail);
        dest.writeString(profileUid);
        dest.writeString(profilePhotoUrl);
        dest.writeString(phoneNumber);
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

    public String getProfileDisplayName() {
        return profileDisplayName;
    }

    public void setProfileDisplayName(String name) {
        this.profileDisplayName = name;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String email) {
        this.profileEmail = email;
    }

    public String getProfileUid() {
        return profileUid;
    }

    public void setProfileUid(String uid) {
        this.profileUid = uid;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
