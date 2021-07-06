package com.example.fooddelivery.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.model.Voucher;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class VoucherAdapter3 extends RecyclerView.Adapter<VoucherAdapter3.VoucherViewHolder3> {

    Context context;
    ArrayList<Voucher> availableVoucherList = new ArrayList<>();

    public VoucherAdapter3() {
    }

    public VoucherAdapter3(Context context, ArrayList<Voucher> vouchers) {
        this.context = context;
        availableVoucherList = vouchers;
    }

    @NonNull
    @Override
    public VoucherViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_voucher_template, parent, false);
        return new VoucherViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder3 holder, int position) {
        Voucher voucher = availableVoucherList.get(position);
        setData(holder, voucher);
        setClickDetails(holder, voucher);
    }

    private void setClickDetails(VoucherViewHolder3 holder, Voucher voucher) {
        holder.ib_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenBottomSheetDialog(voucher);
            }
        });

        holder.ib_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToVoucherList(voucher);
                holder.ib_use.setText(context.getResources().getString(R.string.already_got));
                holder.ib_use.setEnabled(false);
            }
        });
    }


    private void setData(VoucherViewHolder3 holder, Voucher v) {
        holder.imgVoucher.setImageResource(R.drawable.voucher);
        holder.tv_title.setText(v.getTitle());
        holder.tv_date.setText(String.format("%s %s", context.getResources().getString(R.string.HSD), v.getDate()));

        for (Voucher voucher : WelcomeActivity.firebase.voucherList) {
            if (voucher.getId().equals(v.getId())) {
                holder.ib_use.setText(R.string.already_got);
                holder.ib_use.setEnabled(false);
                break;
            }
        }

        if (holder.ib_use.isEnabled()) {
            holder.ib_use.setText(R.string.get);
        }
    }

    @Override
    public int getItemCount() {
        return availableVoucherList != null ?
                availableVoucherList.size() : 0;
    }

    public static final class VoucherViewHolder3 extends RecyclerView.ViewHolder {
        ImageView imgVoucher;
        TextView tv_title, tv_date, ib_use;
        ImageButton ib_details;

        public VoucherViewHolder3(@NonNull View itemView) {
            super(itemView);

            imgVoucher = itemView.findViewById(R.id.vc2_iv_image);
            tv_title = itemView.findViewById(R.id.vc2_tv_title);
            tv_date = itemView.findViewById(R.id.vc2_tv_date);
            ib_details = itemView.findViewById(R.id.vc2_ib_details);
            ib_use = itemView.findViewById(R.id.vc2_ib_use);
        }
    }

    private void addToVoucherList(Voucher v) {
        if (!WelcomeActivity.firebase.voucherList.contains(v)) {
            WelcomeActivity.firebase.addVoucherToList(context, v.getId());
            WelcomeActivity.firebase.voucherList.add(v);
        }
    }

    private void clickOpenBottomSheetDialog(Voucher v) {
        View dialog = LayoutInflater.from(context).inflate(R.layout.voucher_bottom_sheet, null);
        BottomSheetDialog voucherDialog = new BottomSheetDialog(context);
        voucherDialog.setContentView(dialog);
        voucherDialog.show();
        InitDialog(dialog, v);
    }

    private void InitDialog(View dialog, Voucher v) {
        TextView tv_title, tv_code, tv_date, tv_details;
        tv_title = dialog.findViewById(R.id.vc_bs_tv_title);
        tv_code = dialog.findViewById(R.id.vc_bs_tv_code);
        tv_date = dialog.findViewById(R.id.vc_bs_tv_date);
        tv_details = dialog.findViewById(R.id.vc_bs_tv_details);

        tv_title.setText(v.getTitle());
        tv_code.setText(v.getCode());
        tv_date.setText(v.getDate());

        String details = "";
        for (String s : v.getDetails()) {
            details += "•  " + s + "\n\n";
        }
        tv_details.setText(details);
    }
}
