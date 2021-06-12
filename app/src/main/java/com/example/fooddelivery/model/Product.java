package com.example.fooddelivery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private String Id;
    private String Name;
    private String En_Name;
    private String Rating;
    private String Status;
    private Timestamp Create;
    private Merchant Merchant;
    private String Sales;
    private ArrayList<String> Price = new ArrayList<String>();
    private ArrayList<Uri> Image = new ArrayList<Uri>();
    private ArrayList<String> ProductSize = new ArrayList<String>();

    public Product() {
        Id = "sample";
        En_Name = "";
        Name = "Empty";
        Rating = "4.0";
        Status = "InStock";
        Price.add("30000");
        Create = new Timestamp(System.currentTimeMillis());
        Uri uri = Uri.parse("android.resource://com.example.merchanttask/drawable/untitled_icon");
        Image.add(uri);
    }

    public Product(String name, String rating, String status, String price, Uri image, Timestamp create) {
        Id = "sample";
        Name = name;
        En_Name = "";
        Rating = rating;
        Status = status;
        Price.add(price);
        Image.add(image);
        Create = create;
    }

    public Product(String name, Uri image, String rating, String price) {
        Id = "sample";
        Name = name;
        En_Name = "";
        Rating = rating;
        Status = "InStock";
        Price.add(price);
        Image.add(image);
        Create = new Timestamp(System.currentTimeMillis());
    }

    public Product(String name, String rating, String price) {
        Id = "sample";
        Name = name;
        En_Name = "";
        Rating = rating;
        Status = "InStock";
        Price.add(price);
    }

    public void setProductSize(ArrayList<String> productSize) {
        ProductSize = productSize;
    }

    public void setSales(String sales) {
        Sales = sales;
    }

    public Merchant getMerchant() {
        return Merchant;
    }

    public void setMerchant(com.example.fooddelivery.model.Merchant merchant) {
        Merchant = merchant;
    }

    public String getSales() {
        return Sales;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPrice(ArrayList<String> price) {
        Price = price;
    }

    public void setImage(ArrayList<Uri> image) {
        Image = image;
    }

    public ArrayList<String> getPrice() {
        return Price;
    }

    public ArrayList<Uri> getImage() {
        return Image;
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

    public Timestamp getCreate() {
        return Create;
    }

    public void setCreate(Timestamp crete) {
        Create = crete;
    }

    public ArrayList<String> getProductSize() {
        return ProductSize;
    }

    public String getEn_Name() {
        return En_Name;
    }

    public void setEn_Name(String en_Name) {
        En_Name = en_Name;
    }
}
