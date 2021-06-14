package com.example.fooddelivery.model;

public class User {
    String First_Name;
    String Last_Name;
    String Phone_Number;
    String Email;
    String Address;
    String Password;

    public User() {}

    public User(String first_Name, String last_Name, String phone_Number, String email, String address, String password) {
        First_Name = first_Name;
        Last_Name = last_Name;
        Phone_Number = phone_Number;
        Email = email;
        Address = address;
        Password = password;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}