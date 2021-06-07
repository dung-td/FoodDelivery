package com.example.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Voucher;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;


public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {
    Context context;
    List<Voucher> vouchers;
    Button close;

    public VoucherAdapter(){}

    public VoucherAdapter(Context context, List<Voucher> vouchers) {
        this.context = context;
        this.vouchers = vouchers;
    }
    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_template, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher v = vouchers.get(position);
        holder.imgVoucher.setImageResource(R.drawable.highland_logo);
        holder.tv_title.setText(v.getTitle());
        holder.tv_date.setText("HSD: 01/01/2022");
    }

    @Override
    public int getItemCount() {

        return vouchers != null ? vouchers.size() : 0;
    }

    public final class VoucherViewHolder extends RecyclerView.ViewHolder {
        ImageView imgVoucher;
        TextView tv_title, tv_date;
        ImageButton ib_details;
        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);

            imgVoucher = itemView.findViewById(R.id.vc_iv_image);
            tv_title = itemView.findViewById(R.id.vc_tv_title);
            tv_date = itemView.findViewById(R.id.vc_tv_date);
            ib_details = itemView.findViewById(R.id.vc_ib_details);

            ib_details = itemView.findViewById(R.id.vc_ib_details);
            ib_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickOpenBottomSheetDialog();
                }
            });

        }
    }

    private void clickOpenBottomSheetDialog() {
        View dialog =  LayoutInflater.from(context).inflate(R.layout.voucher_bottom_sheet, null);
        BottomSheetDialog voucherDialog = new BottomSheetDialog(context);
        voucherDialog.setContentView(dialog);
        voucherDialog.show();
    }
}
