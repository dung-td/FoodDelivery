package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.model.Voucher;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class VoucherAdapter2 extends RecyclerView.Adapter<VoucherAdapter2.VoucherViewHolder2> {

    Context context;
    ArrayList<Voucher> userAvailableVoucherList = new ArrayList<>();

    public VoucherAdapter2() {
    }

    public VoucherAdapter2(Context context, ArrayList<Voucher> vouchers) {
        this.context = context;
        userAvailableVoucherList = vouchers;
    }

    @NonNull
    @Override
    public VoucherViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_voucher_template, parent, false);
        return new VoucherViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder2 holder, int position) {
        Voucher voucher = userAvailableVoucherList.get(position);
        setData(holder, voucher);
        setClickDetails(holder, voucher);
    }

    private void setClickDetails(VoucherViewHolder2 holder, Voucher voucher) {
        holder.ib_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenBottomSheetDialog(voucher);
            }
        });

        holder.ib_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ib_use.setText(context.getString(R.string.chose));
                useVoucherInCart(voucher);
            }
        });
    }

    private void useVoucherInCart(Voucher v) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("voucherId", v.getId());
        returnIntent.putExtra("voucherCode", v.getCode());
        returnIntent.putExtra("maxDiscount", v.getValues().get(0));
        returnIntent.putExtra("minSumPrice", v.getValues().get(1));
        returnIntent.putExtra("percentDiscount", v.getValues().get(2));
        ((Activity) context).setResult(Activity.RESULT_OK, returnIntent);
        ((Activity) context).finish();
    }

    private void setData(VoucherViewHolder2 holder, Voucher v) {
        holder.imgVoucher.setImageResource(R.drawable.voucher);
        holder.tv_title.setText(v.getTitle());
        holder.tv_date.setText(String.format("%s %s", context.getResources().getString(R.string.HSD), v.getDate()));
    }

    @Override
    public int getItemCount() {
        return userAvailableVoucherList != null ?
                userAvailableVoucherList.size() : 0;
    }

    public final class VoucherViewHolder2 extends RecyclerView.ViewHolder {
        ImageView imgVoucher;
        TextView tv_title, tv_date, ib_use;
        ImageButton ib_details;

        public VoucherViewHolder2(@NonNull View itemView) {
            super(itemView);

            imgVoucher = itemView.findViewById(R.id.vc2_iv_image);
            tv_title = itemView.findViewById(R.id.vc2_tv_title);
            tv_date = itemView.findViewById(R.id.vc2_tv_date);
            ib_details = itemView.findViewById(R.id.vc2_ib_details);
            ib_use = itemView.findViewById(R.id.vc2_ib_use);
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
