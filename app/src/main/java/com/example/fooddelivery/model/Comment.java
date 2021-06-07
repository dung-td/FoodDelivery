package com.example.fooddelivery.model;

public class Comment {
    private final int id;
    private final String user_name;
    private final String comment;
    private final int user_image;

    public String getUser_name() {
        return user_name;
    }

    public String getComment() {
        return comment;
    }

    public int getUser_image() {
        return user_image;
    }

    public String getDate() {
        return date;
    }

    public int getStar() {
        return star;
    }

    private final String date;
    private final int star;

    public Comment(int id, String name, String comment, int img, String date, int star) {
        this.id = id;
        this.user_name = name;
        this.comment = comment;
        this.user_image = img;
        this.date = date;
        this.star = star;
    }

    public int getId() {
        return id;
    }
}
