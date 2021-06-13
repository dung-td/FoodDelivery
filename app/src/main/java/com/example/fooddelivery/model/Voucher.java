package com.example.fooddelivery.model;

import java.util.List;

public class Voucher {
    String Title;
    String Code;
    String Date;
    List<String> Details;
    List<String> Values;
    String Status;

    public Voucher() {
    }

    public Voucher(String title, String code, String date, List<String> details, List<String> values, String status) {
        Title = title;
        Code = code;
        Date = date;
        Details = details;
        Values = values;
        Status = status;
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

    public List<String> getDetails() {
        return Details;
    }

    public void setDetails(List<String> details) {
        Details = details;
    }

    public List<String> getValues() {
        return Values;
    }

    public void setValues(List<String> values) {
        Values = values;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}