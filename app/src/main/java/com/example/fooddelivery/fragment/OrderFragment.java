package com.example.fooddelivery.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.me.LanguageSetting;
import com.example.fooddelivery.adapter.ViewPagerOrdersAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class OrderFragment extends Fragment {

    ViewPager vp_orders;
    TabLayout tl_orders;
    Activity activity;
    String language;

    public OrderFragment(Activity activity, String language)
    {
        this.activity = activity;
        this.language = language;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLocal();
        vp_orders=(ViewPager)getView().findViewById(R.id.fm_order_viewpager);
        tl_orders=(TabLayout)getView().findViewById(R.id.fm_order_tablayout);
        ViewPagerOrdersAdapter viewAdapter = new ViewPagerOrdersAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, activity);

        vp_orders.setAdapter(viewAdapter);
        tl_orders.setupWithViewPager(vp_orders);
    }

    public void setLocal()
    {
        String langCode;
        LanguageSetting languageSetting = new LanguageSetting();
        Log.e("Lang", languageSetting.getChosenLanguege());
        if (language.equals("vi"))
            langCode = "vi";
        else   {
            langCode ="en";
        }

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}