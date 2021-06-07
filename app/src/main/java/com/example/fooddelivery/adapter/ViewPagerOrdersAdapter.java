package com.example.fooddelivery.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fooddelivery.fragment.ListOrdersFragment;

public class ViewPagerOrdersAdapter extends FragmentStatePagerAdapter {
    public ViewPagerOrdersAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
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
                title = "ĐANG ĐẾN";
                break;
            case 1:
                title = "LỊCH SỬ";
                break;
        }
        return title;
    }
}