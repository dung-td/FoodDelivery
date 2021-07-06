package com.example.fooddelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    String iD;
    String userName;
    String date;
    String details;
    String rating;

    public Comment() {
    }

    public Comment(String iD, String userName, String date, String details, String rating) {
        this.iD = iD;
        this.userName = userName;
        this.date = date;
        this.details = details;
        this.rating = rating;
    }

    public Comment(String date, String details, String rating, String userName) {
        this.userName = userName;
        this.date = date;
        this.details = details;
        this.rating = rating;
    }

    public Comment(String date, String details, String rating) {
        this.date = date;
        this.details = details;
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iD);
        dest.writeString(userName);
        dest.writeString(date);
        dest.writeString(details);
        dest.writeString(rating);
    }

    protected Comment(Parcel in) {
        iD = in.readString();
        userName = in.readString();
        date = in.readString();
        details = in.readString();
        rating = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
