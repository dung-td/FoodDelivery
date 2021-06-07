package com.example.fooddelivery.model;

public class ChosenItem {
    private final int id;
    private final int image_id;
    private final String item_name;
    private final String item_size;
    private final int item_price;
    private final int quantity;
    private final String extra_note;
    private final String merchant_name;

    public int getImage_id() {
        return image_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_size() {
        return item_size;
    }

    public int getItem_price() {
        return item_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getExtra_note() {
        return extra_note;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public ChosenItem(int id, int image_id, String item_name, String item_size, int item_price, int quantity, String extra_note, String merchant) {
        this.id = id;
        this.image_id = image_id;
        this.item_name = item_name;
        this.item_size = item_size;
        this.item_price = item_price;
        this.quantity = quantity;
        this.extra_note = extra_note;
        this.merchant_name = merchant;
    }

    public int getId() {
        return id;
    }
}
