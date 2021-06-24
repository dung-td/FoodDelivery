package com.example.fooddelivery.model;

import android.location.Address;
import android.net.Uri;

import java.util.Locale;

public class User {
    String First_Name;
    String Last_Name;
    String Phone_Number;
    String Email;
    Address Address;
    String Password;
    Uri ProfileImage;

    public User() {
        Locale locale = new Locale("Vietnamese", "Vietnam");
        Address = new Address(locale);
    }

    public User(String first_Name, String last_Name, String phone_Number, String email, String password) {
        First_Name = first_Name;
        Last_Name = last_Name;
        Phone_Number = phone_Number;
        Email = email;
        Password = password;
    }

    public User(String first_Name, String last_Name, String phone_Number, String email, Address address, String password) {
        First_Name = first_Name;
        Last_Name = last_Name;
        Phone_Number = phone_Number;
        Email = email;
        Address = address;
        Password = password;
    }

    public User(String first_Name, String last_Name, String phone_Number, String email, Address address, String password, Uri profileImage) {
        First_Name = first_Name;
        Last_Name = last_Name;
        Phone_Number = phone_Number;
        Email = email;
        Address = address;
        Password = password;
        this.ProfileImage = profileImage;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Address getAddress() {
        return Address;
    }

    public void setAddress(Address address) {
        Address = address;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Uri getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(Uri profileImage) {
        this.ProfileImage = profileImage;
    }
}