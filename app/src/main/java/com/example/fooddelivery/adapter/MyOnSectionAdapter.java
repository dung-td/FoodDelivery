package com.example.fooddelivery.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fooddelivery.activity.login.LoginActivity;

public class MyOnSectionAdapter extends FragmentPagerAdapter {

    private final Context myContext;
    int totalTabs;

    public MyOnSectionAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ItemOnSectionFragment itemFragment = new ItemOnSectionFragment(0);
                return itemFragment;
            case 1:
                ItemOnSectionFragment itemFragment2 = new ItemOnSectionFragment(1);
                return itemFragment2;
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