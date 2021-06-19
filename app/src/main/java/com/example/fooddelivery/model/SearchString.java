package com.example.fooddelivery.model;

public class SearchString {

    private String Id;
    private String Detail;

    public SearchString(String id, String detail) {
        Id = id;
        Detail = detail;
    }

    public SearchString() {}

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }
}
