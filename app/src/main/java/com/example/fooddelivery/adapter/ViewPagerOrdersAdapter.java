package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.fragment.GeneralOrdersFragment;
import com.example.fooddelivery.model.OrderStatus;
import com.example.fooddelivery.model.Orders;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;

public class ViewPagerOrdersAdapter extends FragmentStatePagerAdapter {
    Activity activity;
    public ViewPagerOrdersAdapter(@NonNull FragmentManager fm, int behavior, Activity activity) {
        super(fm, behavior);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new GeneralOrdersFragment(getListPending());
            case 1:
                return new GeneralOrdersFragment(getListDelivering());
            case 2:
                return new GeneralOrdersFragment(getListHistory());
            default:
                return new GeneralOrdersFragment(getListHistory());
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title ="";
        switch (position){
            case 0:
                title = activity.getBaseContext().getResources().getString(R.string.pending);
                break;
            case 1:
                title = activity.getBaseContext().getResources().getString(R.string.delivering);
                break;
            case 2:
                title = activity.getBaseContext().getResources().getString(R.string.history);
                break;

        }


        return title;
    }

    ArrayList<Orders> getListDelivering()
    {
        ArrayList<Orders> tmpList = LoginActivity.firebase.ordersList;
        ArrayList<Orders> listDelivering = new ArrayList<>();

        for (Orders orders : tmpList)
        {
            if (orders.getStatus().equals(OrderStatus.Delivering.toString()))
                listDelivering.add(orders);
        }
        return  listDelivering;
    }

    ArrayList<Orders> getListHistory()
    {
        ArrayList<Orders> tmpList = LoginActivity.firebase.ordersList;
        ArrayList<Orders> listHistory = new ArrayList<>();

        for (Orders orders : tmpList)
        {
          //  Log.e("order get status", orders.getStatus());
            if (orders.getStatus().equals(OrderStatus.Succeeded.toString())
            || orders.getStatus().equals(OrderStatus.Canceled.toString()))
                listHistory.add(orders);
        }
        return  listHistory;
    }

    ArrayList<Orders> getListPending()
    {
        ArrayList<Orders> tmpList = LoginActivity.firebase.ordersList;
        ArrayList<Orders> listHistory = new ArrayList<>();

        for (Orders orders : tmpList)
        {
            if (orders.getStatus().equals(OrderStatus.Pending.toString()) ||
                    orders.getStatus().equals(OrderStatus.Received.toString())
            )
                listHistory.add(orders);
        }
        return  listHistory;
    }

}