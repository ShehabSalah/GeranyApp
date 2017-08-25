package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 8/19/17.
 * ReportFeedBack Model
 */

public class ReportFeedBack implements Parcelable{
    private String user_id;
    private String username;
    private String profile_pic;
    private String feedbackId;

    public ReportFeedBack() {
    }

    public ReportFeedBack(String user_id, String username, String profile_pic, String feedbackId) {
        this.user_id        = user_id;
        this.username       = username;
        this.profile_pic    = profile_pic;
        this.feedbackId     = feedbackId;
    }

    protected ReportFeedBack(Parcel in) {
        user_id             = in.readString();
        username            = in.readString();
        profile_pic         = in.readString();
        feedbackId          = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(profile_pic);
        dest.writeString(feedbackId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReportFeedBack> CREATOR = new Creator<ReportFeedBack>() {
        @Override
        public ReportFeedBack createFromParcel(Parcel in) {
            return new ReportFeedBack(in);
        }

        @Override
        public ReportFeedBack[] newArray(int size) {
            return new ReportFeedBack[size];
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

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }
}
