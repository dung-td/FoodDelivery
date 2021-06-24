package com.example.fooddelivery.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fooddelivery.activity.MerchantActivity;
import com.example.fooddelivery.fragment.CommentFragment;
import com.example.fooddelivery.fragment.InfoFragment;
import com.example.fooddelivery.fragment.ItemFragment;

public class MyAdapter extends FragmentPagerAdapter {

    private final Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ItemFragment itemFragment = new ItemFragment(MerchantActivity.merchant);
                return itemFragment;
            case 1:
                InfoFragment commentFragment = new InfoFragment();
                return commentFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}