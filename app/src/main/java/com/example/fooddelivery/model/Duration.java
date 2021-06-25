package com.example.fooddelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Duration implements Parcelable {
    public String text;
    public int value;

    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
    }

    protected Duration(Parcel in) {
        text = in.readString();
        value = in.readInt();
    }

    public static final Creator<Duration> CREATOR = new Creator<Duration>() {
        @Override
        public Duration createFromParcel(Parcel in) {
            return new Duration(in);
        }

        @Override
        public Duration[] newArray(int size) {
            return new Duration[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeInt(value);
    }
}
