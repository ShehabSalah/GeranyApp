package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 8/5/17.
 * My places data model
 */

public class MyPlaces implements Parcelable {
    private String placeId;
    private String userId;
    private String placeAddress;
    private String placeNickname;
    private boolean isActive;

    public MyPlaces() {
    }

    public MyPlaces(String placeId, String userId, String placeAddress, String placeNickname, boolean isActive) {
        this.placeId            = placeId;
        this.userId             = userId;
        this.placeAddress       = placeAddress;
        this.placeNickname      = placeNickname;
        this.isActive           = isActive;
    }

    protected MyPlaces(Parcel in) {
        placeId                 = in.readString();
        userId                  = in.readString();
        placeAddress            = in.readString();
        placeNickname           = in.readString();
        isActive                = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeId);
        dest.writeString(userId);
        dest.writeString(placeAddress);
        dest.writeString(placeNickname);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyPlaces> CREATOR = new Creator<MyPlaces>() {
        @Override
        public MyPlaces createFromParcel(Parcel in) {
            return new MyPlaces(in);
        }

        @Override
        public MyPlaces[] newArray(int size) {
            return new MyPlaces[size];
        }
    };

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceNickname() {
        return placeNickname;
    }

    public void setPlaceNickname(String placeNickname) {
        this.placeNickname = placeNickname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
