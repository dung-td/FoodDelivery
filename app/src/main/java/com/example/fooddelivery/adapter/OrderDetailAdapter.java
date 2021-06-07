package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.OrderItem;

import java.util.ArrayList;

public class OrderDetailAdapter extends BaseAdapter {
    ArrayList<OrderItem> listOrderItem ;
    Activity activity;

    public OrderDetailAdapter(Activity act, ArrayList<OrderItem> orderItems)
    {
        this.activity = act;
        this.listOrderItem = orderItems;
    }
    @Override
    public int getCount() {
        return (listOrderItem == null) ? 0 : listOrderItem.size();
    }

    @Override
    public OrderItem getItem(int position) {
        return listOrderItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.order_details, null);


        ImageView imageView = (ImageView) convertView.findViewById(R.id.im_orddetail_image);
        TextView name = (TextView)convertView.findViewById(R.id.tv_orddetail_productname);
        TextView category = (TextView)convertView.findViewById(R.id.tv_orddetail_productcate);
        TextView quantity = (TextView)convertView.findViewById(R.id.tv_orddetail_quantity);
        TextView price = (TextView)convertView.findViewById(R.id.tv_orddetail_price);

        imageView.setImageResource(R.drawable.trasenvang);
        name.setText(listOrderItem.get(position).getProduct().getName());
        category.setText("Loại hàng");
        // quantity.setText(listOrderItem.get(position).getQuantity());
        quantity.setText("x2");
        price.setText(listOrderItem.get(position).getProduct().getPrice());

        return convertView;

    }
}