package com.example.fooddelivery.model;

public class Comment {
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
