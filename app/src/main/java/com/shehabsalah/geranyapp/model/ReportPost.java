package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 8/19/17.
 * ReportPost Model
 */

public class ReportPost implements Parcelable{
    private String reportPostId;
    private String user_id;
    private String username;
    private String profile_pic;

    public ReportPost(String reportPostId, String user_id, String username, String profile_pic) {
        this.reportPostId   = reportPostId;
        this.user_id        = user_id;
        this.username       = username;
        this.profile_pic    = profile_pic;
    }

    protected ReportPost(Parcel in) {
        reportPostId        = in.readString();
        user_id             = in.readString();
        username            = in.readString();
        profile_pic         = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reportPostId);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(profile_pic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReportPost> CREATOR = new Creator<ReportPost>() {
        @Override
        public ReportPost createFromParcel(Parcel in) {
            return new ReportPost(in);
        }

        @Override
        public ReportPost[] newArray(int size) {
            return new ReportPost[size];
        }
    };

    public String getReportPostId() {
        return reportPostId;
    }

    public void setReportPostId(String reportPostId) {
        this.reportPostId = reportPostId;
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
}
