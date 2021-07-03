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
import com.example.fooddelivery.activity.login.WelcomeActivity;
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
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new GeneralOrdersFragment(WelcomeActivity.firebase.orderList.get(0));
            case 1:
                return new GeneralOrdersFragment(WelcomeActivity.firebase.orderList.get(1));
            case 2:
                return new GeneralOrdersFragment(WelcomeActivity.firebase.orderList.get(2));
            default:
                return new GeneralOrdersFragment(WelcomeActivity.firebase.orderList.get(2));
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
}