package com.example.fooddelivery.model;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    int user_id;
    int shipper_id;
    String status;
    String created_at;
    ArrayList<OrderItem> listOrderItems;

    public Orders(int user_id, int shipper_id, String status, String created_at, ArrayList<OrderItem> listOrderItems) {
        this.user_id = user_id;
        this.shipper_id = shipper_id;
        this.status = status;
        this.created_at = created_at;
        this.listOrderItems = listOrderItems;
    }

    //region GET_SET
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setShipper_id(int shipper_id) {
        this.shipper_id = shipper_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setListOrderItems(ArrayList<OrderItem> listOrderItems) {
        this.listOrderItems = listOrderItems;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getShipper_id() {
        return shipper_id;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public List<OrderItem> getListOrderItems() {
        return listOrderItems;
    }

    public OrderItem getFirstOrderItems() {
        return listOrderItems.get(0);
    }
    //endregion

    public int getTotalPrice()
    {
        int price =0;
        for (int i=0; i<listOrderItems.size(); i++)
        {
            price+=Integer.parseInt(listOrderItems.get(i).getProduct().getPrice().get(0)) * listOrderItems.get(i).quantity;
        }
        return price;
    }

}

