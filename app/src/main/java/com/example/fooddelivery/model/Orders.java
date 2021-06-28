package com.example.fooddelivery.model;

import java.util.ArrayList;

public class Orders {
    String orderID;
    String status;
    String time;
    int discount;
    int freightCost;
    int totalAmount;
    String voucherID;
    String method;
    String user_name;
    String user_address;
    ArrayList<OrderItem> listOrderItems;

    public Orders(){}

    public Orders(String status, String date, int discount, int freightCost, int totalAmount, String voucherID, String method, ArrayList<OrderItem> listOrderItems, String user_address, String user_name) {
        this.status = status;
        this.time = date;
        this.discount = discount;
        this.freightCost = freightCost;
        this.totalAmount = totalAmount;
        this.voucherID = voucherID;
        this.method = method;
        this.listOrderItems = listOrderItems;
        this.user_address=user_address;
        this.user_name = user_name;
    }

    //region GET SET
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getFreightCost() {
        return freightCost;
    }

    public void setFreightCost(int freightCost) {
        this.freightCost = freightCost;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(String voucherID) {
        this.voucherID = voucherID;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String  method) {
        this.method = method;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public ArrayList<OrderItem> getListOrderItems() {
        return listOrderItems;
    }

    public void setListOrderItems(ArrayList<OrderItem> listOrderItems) {
        this.listOrderItems = listOrderItems;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public OrderItem getFirstOrderItems() {
        return listOrderItems.get(0);
    }
    //endregion

    public int getTmpPrice()
    {
        int price = 0;

        for (OrderItem orderItem: listOrderItems)
            price = price + orderItem.getPrice() * orderItem.getQuantity();

        return price;
    }


}

