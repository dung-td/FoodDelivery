package com.example.fooddelivery.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fooddelivery.fragment.VoucherFragment;

public class voucherStatusAdapter extends FragmentStatePagerAdapter {

    public voucherStatusAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
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
                title = "HIỆN CÓ";
                break;
            case 1:
                title = "ĐÃ DÙNG";
                break;
            case 2:
                title = "HẾT HẠN";
                break;
            default:
                title = "HIỆN CÓ";
                break;
        }
        return title;
    }
}
