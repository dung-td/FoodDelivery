package com.example.fooddelivery.model;

import android.location.Address;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Merchant implements Parcelable {
    private String Id;
    private String Name;
    private Address Address;
    private String Email;
    private String Phone;
    private ArrayList<Uri> Image = new ArrayList<Uri>();
    private List<Route> routes = new ArrayList<Route>();

    public Merchant() {
        Locale locale = new Locale("Vietnamese", "Vietnam");
        Address = new Address(locale);
        Id = Name = Email = Phone = "Empty";
        Uri uri = Uri.parse("android.resource://com.example.merchanttask/drawable/untitled_icon");
        Image.add(uri);
    }

    public Merchant(String name, Address address) {
        Name = name;
        Address = address;
        Email = Phone = "Empty";
        Uri uri = Uri.parse("android.resource://com.example.merchanttask/drawable/untitled_icon");
        Image.add(uri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(Email);
        dest.writeString(Phone);
        dest.writeTypedList(Image);
    }

    protected Merchant(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Email = in.readString();
        Phone = in.readString();
        Image = in.createTypedArrayList(Uri.CREATOR);
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Address getAddress() {
        return Address;
    }

    public void setAddress(Address address) {
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

    public ArrayList<Uri> getImage() {
        return Image;
    }

    public void setImage(ArrayList<Uri> image) {
        Image = image;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
