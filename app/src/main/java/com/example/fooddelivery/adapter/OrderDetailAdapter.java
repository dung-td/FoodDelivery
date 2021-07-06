package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.model.OrderItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderDetailAdapter extends BaseAdapter {
    ArrayList<OrderItem> listOrderItem ;
    Activity activity;

    ImageView imageView ;
    TextView name;
    TextView category;
    TextView quantity;
    TextView price;

    public OrderDetailAdapter(Activity act, ArrayList<OrderItem> listOrderItem)
    {
        this.activity = act;
        this.listOrderItem = listOrderItem;
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

        initView(convertView);
        setData(position);

        return convertView;

    }

    void initView(View convertView)
    {
        imageView = (ImageView) convertView.findViewById(R.id.im_orddetail_image);
        name = (TextView)convertView.findViewById(R.id.tv_orddetail_productname);
        category = (TextView)convertView.findViewById(R.id.tv_orddetail_productcate);
        quantity = (TextView)convertView.findViewById(R.id.tv_orddetail_quantity);
        price = (TextView)convertView.findViewById(R.id.tv_orddetail_price);
    }

    void setData(int position)
    {
        Picasso.get().load(listOrderItem.get(position).getProduct().getImage().get(0))
                .into(imageView);

        setName(position);
        category.setText(listOrderItem.get(position).getSize());
        quantity.setText("x"+Integer.toString(listOrderItem.get(position).getQuantity()));
        price.setText(Integer.toString(listOrderItem.get(position).getPrice()));
    }

    void setName(int position){
        if (WelcomeActivity.language.equals("vi"))
            name.setText(listOrderItem.get(position).getProduct().getName());
        else
            name.setText(listOrderItem.get(position).getProduct().getEn_Name());
    }
}