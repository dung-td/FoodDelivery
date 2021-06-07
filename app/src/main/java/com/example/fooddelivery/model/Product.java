package com.example.fooddelivery.model;

import android.net.Uri;
import java.sql.Timestamp;

public class Product {
    private String Name;
    private String Rating;
    private String Status;
    private String Price;
    private Timestamp Create;
    private Uri Image;

    public Product() {
        Name = "Empty";
        Rating = "4.0";
        Status = "InStock";
        Price = "30000";
        Create = new Timestamp(System.currentTimeMillis());
        Uri uri = Uri.parse("android.resource://com.example.merchanttask/drawable/highland_logo");
        Image = uri;
    };

    public Product(String name, String rating, String status, String price, Uri image, Timestamp create) {
        Name = name;
        Rating = rating;
        Status = status;
        Price = price;
        Image = image;
        Create = create;
    }

    public Product(String name,Uri image, String rating, String price) {
        Name = name;
        Rating = rating;
        Status = "InStock";
        Price = price;
        Image = image;
        Create = new Timestamp(System.currentTimeMillis());
    }

    public Product(String name, String rating, String price) {
        Name = name;
        Rating = rating;
        Status = "InStock";
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setImage(Uri imageUrl) {
        Image = imageUrl;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public Timestamp getCreate() {
        return Create;
    }

    public void setCreate(Timestamp crete) {
        Create = crete;
    }

    public Uri getImage() {
        return Image;
    }


}
