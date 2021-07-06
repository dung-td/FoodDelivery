package com.example.fooddelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyNotification implements Parcelable {
    private String title;
    private String desc;
    private String time;
    private String status;
    private String id;

    public MyNotification(){this.id = getId();}

    public MyNotification(String id, String title, String desc, String time) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.time = time;
    }

    protected MyNotification(Parcel in) {
        id = in.readString();
        title = in.readString();
        desc = in.readString();
        time = in.readString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() {
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

    public static final Creator<MyNotification> CREATOR = new Creator<MyNotification>() {
        @Override
        public MyNotification createFromParcel(Parcel in) {
            return new MyNotification(in);
        }

        @Override
        public MyNotification[] newArray(int size) {
            return new MyNotification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(time);
    }
}
