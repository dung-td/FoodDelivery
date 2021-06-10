package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.LanguageSetting;
import com.example.fooddelivery.activity.MainActivity;
import com.example.fooddelivery.fragment.ListOrdersFragment;

import java.util.Locale;

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
                return new ListOrdersFragment();
            case 1:
                return new ListOrdersFragment();
            default:
                return new ListOrdersFragment();
        }
    }

    @Override    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title ="";
        switch (position){
            case 0:
                //title = Resources.getSystem().getString(R.string.delivering);
                title = activity.getBaseContext().getResources().getString(R.string.delivering);

                break;
            case 1:
                title = activity.getBaseContext().getResources().getString(R.string.history);

                break;
        }


        return title;
    }


}