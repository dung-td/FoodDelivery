package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.RatingProductFragment;
import com.example.fooddelivery.model.OrderItem;

import java.util.ArrayList;

public class RatedOrderAdapter extends BaseAdapter {
    ArrayList<OrderItem> listOrderItem ;
    Activity activity;

    public RatedOrderAdapter(Activity act, ArrayList<OrderItem> orderItems)
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
        convertView = inflater.inflate(R.layout.my_rating_list, null);


        ImageView productPhoto = (ImageView) convertView.findViewById(R.id.im_cmt_image);
        TextView name = (TextView)convertView.findViewById(R.id.tv_cmt_productname);
        TextView category = (TextView)convertView.findViewById(R.id.tv_cmt_productcate);
        TextView price = (TextView)convertView.findViewById(R.id.tv_cmt_price);

        RatingBar star = (RatingBar)convertView.findViewById(R.id.cmt_star);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.im_cmt_avatar);
        TextView date = (TextView) convertView.findViewById(R.id.tv_cmt_date);
        TextView comment = (TextView)convertView.findViewById(R.id.tv_cmt_cmt);
        TextView userID = (TextView) convertView.findViewById(R.id.tv_cmt_userID);

        productPhoto.setImageResource(R.drawable.trasenvang);
        name.setText(listOrderItem.get(position).getProduct().getName());
        category.setText("Loại hàng");
        price.setText(listOrderItem.get(position).getProduct().getPrice());
        avatar.setImageResource(R.color.light_blue);
        date.setText("1/1/2021");
        comment.setText("Đây là bình luận");
        userID.setText("userID");

        Button bt_Rate = (Button)convertView.findViewById(R.id.bt_cmt_rating);

        star.setNumStars(4);

        comment.setVisibility(View.INVISIBLE);
        star.setVisibility(View.INVISIBLE);
        avatar.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);
        userID.setVisibility(View.INVISIBLE);

        //bt_Rate.setVisibility(View.VISIBLE);

        bt_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingProductFragment nextFrag= new RatingProductFragment();
//                activity.getFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, nextFrag, null)
//                        .addToBackStack(null)
//                        .commit();
                nextFrag.show(activity.getFragmentManager(), null);
            }
        });

        return convertView;
    }
}