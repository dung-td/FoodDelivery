package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.model.OrderStatus;
import com.example.fooddelivery.model.Orders;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GeneralOrderAdapter extends BaseAdapter {
    ArrayList<Orders> listOrders ;
    Activity activity;

    TextView merchant ;
    ImageView imageView ;
    TextView name;
    TextView category;
    TextView quantity;
    TextView price;
    TextView detail;
    TextView total;
    TextView total_price;
    TextView status;

    public GeneralOrderAdapter(Activity act, ArrayList<Orders> orders)
    {
        this.activity = act;
        this.listOrders = orders;
        notifyDataSetChanged();
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

        initView(convertView);

        setData(position);

        return convertView;
    }

    void initView(View convertView) {
        merchant = (TextView)convertView.findViewById(R.id.tv_ordlist_merchant);
        imageView = (ImageView) convertView.findViewById(R.id.tv_ordlist_image);
        name = (TextView)convertView.findViewById(R.id.tv_ordlist_productname);
        category = (TextView)convertView.findViewById(R.id.tv_ordlist_productcate);
        quantity = (TextView)convertView.findViewById(R.id.tv_ordlist_quantity);
        price = (TextView)convertView.findViewById(R.id.tv_ordlist_price);
        detail = (TextView)convertView.findViewById(R.id.tv_ordlist_details);
        total = (TextView)convertView.findViewById(R.id.tv_ordlist_total);
        total_price = (TextView)convertView.findViewById(R.id.tv_ordlist_totalprice);
        status = (TextView)convertView.findViewById(R.id.tv_ordlist_status);
    }

    void setData(int position)
    {
        merchant.setText(listOrders.get(position).getFirstOrderItems().getProduct()
                .getMerchant().getName());

        Picasso.get().load(listOrders.get(position).getFirstOrderItems().getProduct().getImage().get(0))
                .into(imageView);

        setNameItem(position);

        category.setText(listOrders.get(position).getFirstOrderItems().getSize());


        quantity.setText("x"
                + Integer.toString(listOrders.get(position).getFirstOrderItems().getQuantity()));

        price.setText(Integer.toString(listOrders.get(position).getFirstOrderItems().getPrice()));

        if(listOrders.get(position).getListOrderItems().size() > 1)
            detail.setText(R.string.continue_);
        else
            detail.setText("");

        total_price.setText(Integer.toString(listOrders.get(position).getTotalAmount()));

        setStatus(position);

    }

    void setNameItem(int position)
    {
        if (LoginActivity.language.equals("vi"))
            name.setText(String.format(listOrders.get(position).getFirstOrderItems().getProduct().getName()));
        else
            name.setText(String.format(listOrders.get(position).getFirstOrderItems().getProduct().getEn_Name()));
    }

    void setStatus(int position)
    {
        String orderStatus = listOrders.get(position).getStatus();

        if (orderStatus.equals(OrderStatus.Canceled.toString()))
            status.setText(R.string.canceled);

        if (orderStatus.equals(OrderStatus.Pending.toString()))
            status.setText(R.string.pending);

        if (orderStatus.equals(OrderStatus.Succeeded.toString()))
            status.setText(R.string.delivery_success);

        if (orderStatus.equals(OrderStatus.Confirmed.toString()))
            status.setText(R.string.received_order);

        if (orderStatus.equals(OrderStatus.Delivering.toString()))
            status.setText(R.string.delivering);

    }
}