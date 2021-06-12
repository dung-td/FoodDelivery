package com.example.fooddelivery.model;

public class Voucher {
    String Title;
    String Code;
    String Date;
    String[] Details;

    public Voucher() {
    }

    public Voucher(String title, String code, String date, String[] details) {
        Title = title;
        Code = code;
        Date = date;
        Details = details;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String[] getDetails() {
        return Details;
    }

    public void setDetails(String[] details) {
        Details = details;
    }
}