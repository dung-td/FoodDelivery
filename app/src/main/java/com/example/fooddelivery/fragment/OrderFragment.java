package com.example.fooddelivery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.R;
import com.example.fooddelivery.adapter.ViewPagerOrdersAdapter;
import com.google.android.material.tabs.TabLayout;

public class OrderFragment extends Fragment {

    ViewPager vp_orders;
    TabLayout tl_orders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vp_orders=(ViewPager)getView().findViewById(R.id.fm_order_viewpager);
        tl_orders=(TabLayout)getView().findViewById(R.id.fm_order_tablayout);
        ViewPagerOrdersAdapter viewAdapter = new ViewPagerOrdersAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vp_orders.setAdapter(viewAdapter);
        tl_orders.setupWithViewPager(vp_orders);
    }
}