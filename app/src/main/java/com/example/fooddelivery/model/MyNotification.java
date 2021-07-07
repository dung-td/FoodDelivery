package com.example.fooddelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyNotification implements Parcelable {
    private String title_vn;
    private String title_en;
    private String desc_vn;
    private String desc_en;
    private String time;
    private String status;
    private String id;

    public MyNotification(){this.id = getId();}

    public MyNotification(String id, String title_vn, String desc_vn, String time) {
        this.id = id;
        this.title_vn = title_vn;
        this.desc_vn = desc_vn;
        this.time = time;
    }

    protected MyNotification(Parcel in) {
        title_vn = in.readString();
        title_en = in.readString();
        desc_vn = in.readString();
        desc_en = in.readString();
        time = in.readString();
        status = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title_vn);
        dest.writeString(title_en);
        dest.writeString(desc_vn);
        dest.writeString(desc_en);
        dest.writeString(time);
        dest.writeString(status);
        dest.writeString(id);
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

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getDesc_en() {
        return desc_en;
    }

    public void setDesc_en(String desc_en) {
        this.desc_en = desc_en;
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

    public void setTitle_vn(String title_vn) {
        this.title_vn = title_vn;
    }

    public void setDesc_vn(String desc_vn) {
        this.desc_vn = desc_vn;
    }

    public String getTitle_vn() { return this.title_vn; }

    public String getDesc_vn() { return this.desc_vn; }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
