package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
    @SuppressLint("StaticFieldLeak")
    public static GeneralOrdersFragment adapter0 = new GeneralOrdersFragment(0);
    @SuppressLint("StaticFieldLeak")
    public static GeneralOrdersFragment adapter1 = new GeneralOrdersFragment(1);
    @SuppressLint("StaticFieldLeak")
    public static GeneralOrdersFragment adapter2 = new GeneralOrdersFragment(2);

    public ViewPagerOrdersAdapter(@NonNull FragmentManager fm, int behavior, Activity activity) {
        super(fm, behavior);
        this.activity = activity;
        Log.e("Pager", "OnCreate");
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.e("Switch", "Postion" + position);
        switch (position) {
            case 0:
                return adapter0;
            case 1:
                return adapter1;
            case 2:
                return adapter2;
            default:
                return adapter2;
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