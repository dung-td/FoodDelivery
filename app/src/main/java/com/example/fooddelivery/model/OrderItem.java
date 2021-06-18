package com.example.fooddelivery.model;

public class OrderItem {
    String order_id;
    Product product;
    int quantity;
    int price;
    String size;

    Comment comment;

    public OrderItem(String order_id, Product product, int quantity, int price, Comment comment, String size) {
        this.order_id = order_id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.comment = comment;
        this.size = size;
    }

    public OrderItem(){}

    //region GET_SET
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    //endregion
}
