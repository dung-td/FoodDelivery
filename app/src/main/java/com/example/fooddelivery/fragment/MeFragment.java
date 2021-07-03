package com.example.fooddelivery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.activity.me.PaymentMethodActivity;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.me.FeedbackActivity;
import com.example.fooddelivery.activity.me.PersonalInfoActivity;
import com.example.fooddelivery.activity.me.SettingActivity;
import com.example.fooddelivery.activity.me.VoucherActivity;
import com.example.fooddelivery.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class MeFragment extends Fragment {

    ImageButton bt_setting, bt_voucher, bt_logout, bt_feedback, bt_info, bt_payment;
    ImageView imageUser;
    TextView tv_userName;
    String uriAvatar;

    boolean shouldRefreshOnResume;

    public MeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Init();


        loadInfo();

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
                FirebaseAuth.getInstance().signOut();
                Intent loginActivity = new Intent(MeFragment.super.getContext(), LoginActivity.class);
                loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginActivity);
            }
        });

        bt_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voucherActivity = new Intent(MeFragment.super.getContext(), VoucherActivity.class);
                startActivity(voucherActivity);
            }
        });

        bt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent personalInfoActivity = new Intent(MeFragment.super.getContext(), PersonalInfoActivity.class);
                startActivity(personalInfoActivity);
            }
        });

        bt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentMethod = new Intent(MeFragment.super.getContext(), PaymentMethodActivity.class);
                startActivity(paymentMethod);
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

        imageUser = getView().findViewById(R.id.img_user);

        tv_userName = getView().findViewById(R.id.me_fl_name);
    }

    void loadInfo() {
        User temp = WelcomeActivity.firebase.getUser();
        tv_userName.setText(String.format("%s %s", temp.getFirst_Name(), temp.getLast_Name()));
        Glide.with(requireActivity()).load(temp.getProfileImage()).into(imageUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        User temp = WelcomeActivity.firebase.getUser();
        Glide.with(requireActivity()).load(temp.getProfileImage()).into(imageUser);
    }
}
