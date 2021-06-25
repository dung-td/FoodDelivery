package com.example.fooddelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route implements Parcelable{
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;
    public List<LatLng> points;

    public Route() {
    }

    protected Route(Parcel in) {
        endAddress = in.readString();
        endLocation = in.readParcelable(LatLng.class.getClassLoader());
        startAddress = in.readString();
        startLocation = in.readParcelable(LatLng.class.getClassLoader());
        points = in.createTypedArrayList(LatLng.CREATOR);
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(endAddress);
        dest.writeParcelable(endLocation, flags);
        dest.writeString(startAddress);
        dest.writeParcelable(startLocation, flags);
        dest.writeTypedList(points);
    }
}