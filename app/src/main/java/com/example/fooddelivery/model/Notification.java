package com.example.fooddelivery.model;

public class Notification {
    private String title;
    private String desc;
    private String time;

    public Notification(int id, String title, String desc, String time) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.time = time;
    }

    private final int id;

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() { return this.title; }

    public String getDesc() { return this.desc; }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
