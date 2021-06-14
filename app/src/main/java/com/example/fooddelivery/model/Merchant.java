package com.example.fooddelivery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Merchant {
    private String Id;
    private String Name;
    private String Address;
    private String Email;
    private String Phone;
    private ArrayList<Uri> Image = new ArrayList<Uri>();

    public Merchant() {
        Id = Name = Address = Email = Phone = "Empty";
        Uri uri = Uri.parse("android.resource://com.example.merchanttask/drawable/untitled_icon");
        Image.add(uri);
    }

    public Merchant(String name, String address) {
        Name = name;
        Address = address;
        Email = Phone = "Empty";
        Uri uri = Uri.parse("android.resource://com.example.merchanttask/drawable/untitled_icon");
        Image.add(uri);
    }

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

    public ArrayList<Uri> getImage() {
        return Image;
    }

    public void setImage(ArrayList<Uri> image) {
        Image = image;
    }
}
