package com.example.fooddelivery.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fooddelivery.fragment.CommentFragment;
import com.example.fooddelivery.fragment.InfoFragment;
import com.example.fooddelivery.fragment.ItemFragment;
import com.example.fooddelivery.model.Merchant;

public class MyAdapter extends FragmentPagerAdapter {

    Merchant merchant;
    private final Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs, Merchant merchant) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.merchant = merchant;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ItemFragment itemFragment = new ItemFragment();
                return itemFragment;
            case 1:
                CommentFragment commentFragment = new CommentFragment();
                return commentFragment;
            case 2:
                InfoFragment infoFragment = new InfoFragment(merchant);
                return infoFragment;
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