package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Orders;

import java.util.ArrayList;

public class OrderProductAdapter extends BaseAdapter {
    ArrayList<Orders> listOrders ;
    Activity activity;

    public OrderProductAdapter(Activity act, ArrayList<Orders> orders)
    {
        this.activity = act;
        this.listOrders = orders;
    }

    @Override
    public int getCount() {
        return (listOrders == null) ? 0 : listOrders.size();
    }

    @Override
    public Orders getItem(int position) {
        return listOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.order_list, null);

        TextView merchant = (TextView)convertView.findViewById(R.id.tv_ordlist_merchant);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.tv_ordlist_image);
        TextView name = (TextView)convertView.findViewById(R.id.tv_ordlist_productname);
        TextView category = (TextView)convertView.findViewById(R.id.tv_ordlist_productcate);
        TextView quantity = (TextView)convertView.findViewById(R.id.tv_ordlist_quantity);
        TextView price = (TextView)convertView.findViewById(R.id.tv_ordlist_price);
        TextView detail = (TextView)convertView.findViewById(R.id.tv_ordlist_details);
        TextView total = (TextView)convertView.findViewById(R.id.tv_ordlist_total);
        TextView total_price = (TextView)convertView.findViewById(R.id.tv_ordlist_totalprice);
        TextView status = (TextView)convertView.findViewById(R.id.tv_ordlist_status);

        //merchant.setText(listOrders.get(position).getFristOrderItems().getProduct().getMerchant_id());
        merchant.setText(listOrders.get(position).getFirstOrderItems().getProduct().getName());
        imageView.setImageResource(R.drawable.trasenvang);
        name.setText(listOrders.get(position).getFirstOrderItems().getProduct().getName());
        category.setText("Loại hàng");
        quantity.setText("x2");
        price.setText(listOrders.get(position).getFirstOrderItems().getProduct().getPrice().get(0));
        detail.setText("Còn 1 sản phẩm");
        total.setText(" Tổng cộng");
        //total_price.setText(listOrders.get(position).getTotalPrice());
        total_price.setText("100000");
        status.setText("OrderStatus.Delivering");

        return convertView;
    }
}