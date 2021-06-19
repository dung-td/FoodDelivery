package com.example.fooddelivery.model;

import com.google.firebase.Timestamp;
import com.google.firestore.v1.StructuredQuery;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orders {
    String orderID;
    String status;
    Timestamp date;
    int discount;
    int freightCost;
    int totalAmount;
    String voucherID;
    String method;
    ArrayList<OrderItem> listOrderItems;

    public Orders(){}

    public Orders(String status, Timestamp date, int discount, int freightCost, int totalAmount, String voucherID, String method, ArrayList<OrderItem> listOrderItems) {
        this.status = status;
        this.date = date;
        this.discount = discount;
        this.freightCost = freightCost;
        this.totalAmount = totalAmount;
        this.voucherID = voucherID;
        this.method = method;
        this.listOrderItems = listOrderItems;
    }

    //region GET SET
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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

