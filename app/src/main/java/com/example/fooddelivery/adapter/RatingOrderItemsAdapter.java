package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.fragment.ListItemCommentFragment;
import com.example.fooddelivery.fragment.RatingItemFragment;
import com.example.fooddelivery.model.Comment;
import com.example.fooddelivery.model.OrderItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RatingOrderItemsAdapter extends BaseAdapter {
    ArrayList<OrderItem> listOrderItem ;
    Activity activity;
    FragmentManager fragmentManager;
    ListItemCommentFragment listItemCommentFragment;

    ImageView productPhoto ;
    TextView name ;
    TextView category ;
    TextView price;

    RatingBar star;
    ImageView avatar;
    TextView date;
    TextView comment;
    TextView userID ;
    Button bt_Rate;

    static int LIST_RATING_FRAGMENT_CODE = 123456789;

    public RatingOrderItemsAdapter(Activity act, ArrayList<OrderItem> orderItems, FragmentManager fragmentManager, ListItemCommentFragment listItemCommentFragment)
    {
        this.activity = act;
        this.listOrderItem = orderItems;
        this.fragmentManager = fragmentManager;
        this.listItemCommentFragment = listItemCommentFragment;
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


        initView(convertView);

        if (listOrderItem.get(position).getComment().getiD().equals("null"))
            setRating();
        else
            setRated(position);

        setData(position);

        bt_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRatingItemFragment(listOrderItem.get(position), position);

            }
        });

        return convertView;
    }

    void openRatingItemFragment(OrderItem orderItem, int position) {
        RatingItemFragment nextFrag = new RatingItemFragment(orderItem, listOrderItem, position);
        Log.e("position", Integer.toString(position));
        nextFrag.setTargetFragment(listItemCommentFragment, LIST_RATING_FRAGMENT_CODE);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    void initView(View convertView) {
         productPhoto = (ImageView) convertView.findViewById(R.id.im_cmt_image);
         name = (TextView)convertView.findViewById(R.id.tv_cmt_productname);
         category = (TextView)convertView.findViewById(R.id.tv_cmt_productcate);
         price = (TextView)convertView.findViewById(R.id.tv_cmt_price);

         star = (RatingBar)convertView.findViewById(R.id.cmt_star);
         avatar = (ImageView) convertView.findViewById(R.id.im_cmt_avatar);
         date = (TextView) convertView.findViewById(R.id.tv_cmt_date);
         comment = (TextView)convertView.findViewById(R.id.tv_cmt_cmt);
         userID = (TextView) convertView.findViewById(R.id.tv_cmt_userID);

         bt_Rate = (Button)convertView.findViewById(R.id.bt_cmt_rating);
    }

    void setRated(int position) {
        avatar.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        comment.setVisibility(View.VISIBLE);
        userID.setVisibility(View.VISIBLE);
        star.setVisibility(View.VISIBLE);

        Picasso.get().load(LoginActivity.firebase.getUser().getProfileImage())
                .into(avatar);


        date.setText(listOrderItem.get(position).getComment().getDate());
        comment.setText(listOrderItem.get(position).getComment().getDetails());
        userID.setText(listOrderItem.get(position).getComment().getUserName());

        star.setRating(Integer.parseInt(listOrderItem.get(position).getComment().getRating()));

        bt_Rate.setVisibility(View.GONE);
    }

    void setRating() {
        avatar.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        comment.setVisibility(View.GONE);
        userID.setVisibility(View.GONE);
        star.setVisibility(View.GONE);

        bt_Rate.setVisibility(View.VISIBLE);
    }

    void setData(int position) {
        Picasso.get().load(listOrderItem.get(position).getProduct().getImage().get(0))
                .into(productPhoto);
        setNameItem(position);
        category.setText(listOrderItem.get(position).getSize()); ;
        price.setText(Integer.toString(listOrderItem.get(position).getPrice()));
    }

    void setNameItem(int position) {
        if (LoginActivity.language.equals("vi"))
            name.setText(String.format(listOrderItem.get(position).getProduct().getName()));
        else
            name.setText(String.format(listOrderItem.get(position).getProduct().getEn_Name()));
    }
}