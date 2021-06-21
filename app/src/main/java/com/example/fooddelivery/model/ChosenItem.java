package com.example.fooddelivery.model;

public class ChosenItem {

    private Product Product;
    private String Size;
    private String Quantity;

    public ChosenItem() {
        Product = new Product();
    }

    public ChosenItem(Product p, String size, String quantity) {
        Product = p;
        Size = size;
        Quantity = quantity;
    }

    public com.example.fooddelivery.model.Product getProduct() {
        return Product;
    }

    public void setProduct(com.example.fooddelivery.model.Product product) {
        Product = product;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
