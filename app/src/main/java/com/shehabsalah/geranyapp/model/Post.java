package com.shehabsalah.geranyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShehabSalah on 8/19/17.
 * Post Model
 */

public class Post implements Parcelable{
    private String username;
    private String user_id;
    private String profile_picture;
    private String location;
    private String text;
    private String image_url;
    private String date;
    private int numberOfDislikes;
    private int numberOfVolunteers;
    private int numberOfDonation;
    private String category_name;

    public Post() {
    }

    public Post(String username, String user_id, String profile_picture, String location, String text, String image_url, String date, int numberOfDislikes, int numberOfVolunteers, int numberOfDonation, String category_name) {
        this.username               = username;
        this.user_id                = user_id;
        this.profile_picture        = profile_picture;
        this.location               = location;
        this.text                   = text;
        this.image_url              = image_url;
        this.date                   = date;
        this.numberOfDislikes       = numberOfDislikes;
        this.numberOfVolunteers     = numberOfVolunteers;
        this.numberOfDonation       = numberOfDonation;
        this.category_name          = category_name;
    }

    public Post(Parcel in){
        String[] data = new String[11];
        in.readStringArray(data);
        this.username               = data[0];
        this.user_id                = data[1];
        this.profile_picture        = data[2];
        this.location               = data[3];
        this.text                   = data[4];
        this.image_url              = data[5];
        this.date                   = data[6];
        this.numberOfDislikes       = Integer.parseInt(data[7]);
        this.numberOfVolunteers     = Integer.parseInt(data[8]);
        this.numberOfDonation       = Integer.parseInt(data[9]);
        this.category_name          = data[10];
    }
    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.username,
                this.user_id,
                this.profile_picture,
                this.location,
                this.text,
                this.image_url,
                this.date,
                String.valueOf(this.numberOfDislikes),
                String.valueOf(this.numberOfVolunteers),
                String.valueOf(this.numberOfDonation),
                this.category_name
        });
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("user_id",user_id);
        result.put("profile_picture", profile_picture);
        result.put("location", location);
        result.put("text", text);
        result.put("image_url", image_url);
        result.put("date",date);
        result.put("numberOfDislikes", numberOfDislikes);
        result.put("numberOfVolunteers", numberOfVolunteers);
        result.put("numberOfDonation", numberOfDonation);
        result.put("category_name", category_name);

        return result;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumberOfDislikes() {
        return numberOfDislikes;
    }

    public void setNumberOfDislikes(int numberOfDislikes) {
        this.numberOfDislikes = numberOfDislikes;
    }

    public int getNumberOfVolunteers() {
        return numberOfVolunteers;
    }

    public void setNumberOfVolunteers(int numberOfVolunteers) {
        this.numberOfVolunteers = numberOfVolunteers;
    }

    public int getNumberOfDonation() {
        return numberOfDonation;
    }

    public void setNumberOfDonation(int numberOfDonation) {
        this.numberOfDonation = numberOfDonation;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

}
