package com.example.fooddelivery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.VoucherAdapter;
import com.example.fooddelivery.model.Voucher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class VoucherFragment extends Fragment {

    int position;
    ArrayList<Voucher> listAvailable = new ArrayList<Voucher>();
    ArrayList<Voucher> listUsed = new ArrayList<Voucher>();
    ArrayList<Voucher> listExpired = new ArrayList<Voucher>();
    public VoucherFragment() {

    }

    public VoucherFragment(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_voucher, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitList();
        Init();
    }

    private void InitList() {
        for (Voucher v : LoginActivity.firebase.voucherList) {
            switch (v.getStatus()) {
                case "Hiện có":
                    listAvailable.add(v);
                    break;
                case "Đã dùng":
                    listUsed.add(v);
                    break;
                case "Hết hạn":
                    listExpired.add(v);
                    break;
            }
        }
    }

    private void Init() {
        RecyclerView voucher = (RecyclerView)getView().findViewById(R.id.voucherRecyclerView);
        VoucherAdapter voucherAdapter;
        switch (position) {
            case 0:
                voucherAdapter = new VoucherAdapter(getContext(), listAvailable);
                break;
            case 1:
                voucherAdapter = new VoucherAdapter(getContext(), listUsed);
                break;
            case 2:
                voucherAdapter = new VoucherAdapter(getContext(), listExpired);
                break;
            default:
                voucherAdapter = new VoucherAdapter(getContext(), listAvailable);
                break;
        }
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        voucher.setLayoutManager(linearLayoutManager2);
        voucher.setAdapter(voucherAdapter);
    }
}
