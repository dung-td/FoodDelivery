package com.example.fooddelivery.model;

public class OrderItem {
    int order_id;
    Product product;
    int quantity;

    public OrderItem(int order_id, Product product, int quantity) {
        this.order_id = order_id;
        this.product = product;
        this.quantity = quantity;
    }

    //region GET_SET
    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
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
    //endregion
}
