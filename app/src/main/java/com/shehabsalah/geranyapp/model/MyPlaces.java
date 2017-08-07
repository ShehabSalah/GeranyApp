package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 8/5/17.
 * My places data model
 */

public class MyPlaces implements Parcelable {
    private String id;
    private String placeAddress;
    private String placeNickname;
    private boolean isActive;

    public MyPlaces(String placeAddress, String placeNickname, boolean isActive) {
        this.placeAddress = placeAddress;
        this.placeNickname = placeNickname;
        this.isActive = isActive;
    }

    protected MyPlaces(Parcel in) {
        placeAddress = in.readString();
        placeNickname = in.readString();
        isActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}