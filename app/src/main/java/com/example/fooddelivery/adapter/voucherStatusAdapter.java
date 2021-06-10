package com.example.fooddelivery.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.VoucherFragment;

public class voucherStatusAdapter extends FragmentStatePagerAdapter {
    Activity activity;
    public voucherStatusAdapter(@NonNull FragmentManager fm, int behavior, Activity activity) {
        super(fm, behavior);
        this.activity = activity;
    }


    @Override
    public Fragment getItem(int position) {
        return new VoucherFragment(position);
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = activity.getBaseContext().getResources().getString(R.string.now_available);
                break;
            case 1:
                title = activity.getBaseContext().getResources().getString(R.string.used);
                break;
            case 2:
                title = activity.getBaseContext().getResources().getString(R.string.expired);
                break;
            default:
                title = activity.getBaseContext().getResources().getString(R.string.now_available);
                break;
        }
        return title;
    }
}
