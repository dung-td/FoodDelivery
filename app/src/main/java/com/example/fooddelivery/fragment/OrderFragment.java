package com.example.fooddelivery.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.me.LanguageSetting;
import com.example.fooddelivery.adapter.ViewPagerOrdersAdapter;
import com.example.fooddelivery.model.OnGetDataListener;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class OrderFragment extends Fragment {

    ProgressDialog progressDialog;
    ViewPager vp_orders;
    TabLayout tl_orders;
    Activity activity;

    public OrderFragment(Activity activity) {
        this.activity = activity;
        getOrderChangeListener();
    }

    private void getOrderChangeListener() {
        LoginActivity.firebase.OrderStatusChange(new OnGetDataListener() {
            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(activity.getResources().getString(R.string.data_loading));
                progressDialog.show();
            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Log.e("Reload", "test");
                ViewPagerOrdersAdapter viewAdapter = new ViewPagerOrdersAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, activity);
                vp_orders.setAdapter(viewAdapter);
                vp_orders.setSaveEnabled(true);
                viewAdapter.notifyDataSetChanged();
                tl_orders.setupWithViewPager(vp_orders);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLocal();
        vp_orders = view.findViewById(R.id.fm_order_viewpager);
        tl_orders = view.findViewById(R.id.fm_order_tablayout);
        ViewPagerOrdersAdapter viewAdapter = new ViewPagerOrdersAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, activity);
        vp_orders.setSaveEnabled(true);
        vp_orders.setAdapter(viewAdapter);
        tl_orders.setupWithViewPager(vp_orders);
    }

    public void setLocal() {
        String langCode;
        LanguageSetting languageSetting = new LanguageSetting();
        Log.e("Lang", languageSetting.getChosenLanguege());
        if (LoginActivity.language.equals("vi"))
            langCode = "vi";
        else {
            langCode = "en";
        }

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}