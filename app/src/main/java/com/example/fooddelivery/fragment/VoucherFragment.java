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
import com.example.fooddelivery.adapter.VoucherAdapter;
import com.example.fooddelivery.model.Voucher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoucherFragment extends Fragment {

    ImageButton details;
    int position;
    List<Voucher> vouchers;

    public VoucherFragment() {

    }

    public VoucherFragment(int position) {
        this.position = position;
        String[] details = new String[2];
        details[0] = "Áp dụng cho app";
        details[1] = "Áp dụng mua lần đầu";
        vouchers = new ArrayList<>();
        vouchers.add(new Voucher("GIẢM 10K", "DISCOUNT10K", new Date().toString(), details));
        vouchers.add(new Voucher("GIẢM 10K", "DISCOUNT10K", new Date().toString(), details));
        vouchers.add(new Voucher("GIẢM 10K", "DISCOUNT10K", new Date().toString(), details));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_voucher, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Init();
    }

    private void Init() {

        RecyclerView voucher = (RecyclerView)getView().findViewById(R.id.voucherRecyclerView);
        VoucherAdapter voucherAdapter = new VoucherAdapter(getContext(), vouchers);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        voucher.setLayoutManager(linearLayoutManager2);
        voucher.setAdapter(voucherAdapter);
    }
}
