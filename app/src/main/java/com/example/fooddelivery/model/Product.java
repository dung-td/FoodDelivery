package com.example.fooddelivery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Product implements Parcelable {
    private String Id;
    private String Name;
    private String Rating;
    private String Status;
    private Timestamp Create;
    private String Merchant;
    private String Sales;
    private ArrayList<String> Price = new ArrayList<String>();
    private ArrayList<Uri> Image = new ArrayList<Uri>();
    private ArrayList<String> ProductSize = new ArrayList<String>();

    public Product() {
        Id = "sample";
        Name = "Empty";
        Rating = "4.0";
        Status = "InStock";
        Price.add("30000");
        Create = new Timestamp(System.currentTimeMillis());
        Uri uri = Uri.parse("android.resource://com.example.merchanttask/drawable/highland_logo");
        Image.add(uri);
    };

    public Product(String name, String rating, String status, String price, Uri image, Timestamp create) {
        Id = "sample";
        Name = name;
        Rating = rating;
        Status = status;
        Price.add(price);
        Image.add(image);
        Create = create;
    }

    public Product(String name,Uri image, String rating, String price) {
        Id = "sample";
        Name = name;
        Rating = rating;
        Status = "InStock";
        Price.add(price);
        Image.add(image);
        Create = new Timestamp(System.currentTimeMillis());
    }

    public Product(String name, String rating, String price) {
        Id = "sample";
        Name = name;
        Rating = rating;
        Status = "InStock";
        Price.add(price);
    }

    protected Product(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Rating = in.readString();
        Status = in.readString();
        Merchant = in.readString();
        Sales = in.readString();
        Price = in.createStringArrayList();
        Image = in.createTypedArrayList(Uri.CREATOR);
        ProductSize = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(Rating);
        dest.writeString(Status);
        dest.writeString(Merchant);
        dest.writeString(Sales);
        dest.writeStringList(Price);
        dest.writeTypedList(Image);
        dest.writeStringList(ProductSize);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void setProductSize(ArrayList<String> productSize) {
        ProductSize = productSize;
    }

    public void setMerchant(String merchant) {
        Merchant = merchant;
    }

    public void setSales(String sales) {
        Sales = sales;
    }

    public String getMerchant() {
        return Merchant;
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
}
