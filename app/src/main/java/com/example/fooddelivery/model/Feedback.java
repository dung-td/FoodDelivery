package com.example.fooddelivery.model;

public class Feedback {
    String userId;
    String subject;
    String details;
    String date;

    public Feedback() {
    }

    public Feedback(String userId, String subject, String details, String date) {
        this.userId = userId;
        this.subject = subject;
        this.details = details;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
