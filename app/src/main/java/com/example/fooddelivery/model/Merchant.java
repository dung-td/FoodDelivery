package com.example.fooddelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Merchant implements Parcelable {
    private String Name;
    private String Address;
    private String Email;
    private String Phone;

    public Merchant() {
        Name = Address = Email = Phone = "Empty";
    }

    public Merchant(String name, String address) {
        Name = name;
        Address = address;
        Email = Phone = "Empty";
    }

    protected Merchant(Parcel in) {
        Name = in.readString();
        Address = in.readString();
        Email = in.readString();
        Phone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Address);
        dest.writeString(Email);
        dest.writeString(Phone);
    }

    public static final Creator<Merchant> CREATOR = new Creator<Merchant>() {
        @Override
        public Merchant createFromParcel(Parcel in) {
            return new Merchant(in);
        }

        @Override
        public Merchant[] newArray(int size) {
            return new Merchant[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
