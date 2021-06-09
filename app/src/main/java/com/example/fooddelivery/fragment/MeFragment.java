package com.example.fooddelivery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.me.FeedbackActivity;
import com.example.fooddelivery.activity.me.SettingActivity;
import com.example.fooddelivery.activity.me.VoucherActivity;

public class MeFragment extends Fragment {

    ImageButton bt_setting, bt_voucher, bt_logout, bt_feedback, bt_info, bt_payment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Init();

        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingActivity = new Intent(MeFragment.super.getContext(), SettingActivity.class);
                startActivity(settingActivity);
            }
        });

        bt_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackActivity = new Intent(MeFragment.super.getContext(), FeedbackActivity.class);
                startActivity(feedbackActivity);
            }
        });

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bt_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voucherActivity = new Intent(MeFragment.super.getContext(), VoucherActivity.class);
                startActivity(voucherActivity);
            }
        });


    }

    private void Init() {
        bt_setting = getView().findViewById(R.id.me_ib_setting);
        bt_voucher = getView().findViewById(R.id.me_ib_voucher);
        bt_logout = getView().findViewById(R.id.me_ib_logout);
        bt_feedback = getView().findViewById(R.id.me_ib_feedback);
        bt_info = getView().findViewById(R.id.me_ib_info);
        bt_payment = getView().findViewById(R.id.me_ib_payment);
    }
}
