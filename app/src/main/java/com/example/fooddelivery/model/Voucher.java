package com.example.fooddelivery.model;

import java.util.List;

public class Voucher {
    String id;
    String title;
    String code;
    String date;
    List<String> details;
    List<String> values;
    String status;

    public Voucher() {
    }

    public Voucher(String id, String title, String code, String date, List<String> details, List<String> values, String status) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.date = date;
        this.details = details;
        this.values = values;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}