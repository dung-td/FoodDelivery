package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.ProductActivity;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.main.CartActivity;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.ChosenItem;
import com.example.fooddelivery.model.OnGetDataListener;

import java.util.ArrayList;

public class ChosenItemAdapter extends RecyclerView.Adapter<ChosenItemAdapter.ChosenItemViewHolder> {

    private final Context context;
    private final ArrayList<ChosenItem> items;

    public ChosenItemAdapter(Context context, ArrayList<ChosenItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ChosenItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chosen_item_layout, parent, false);
        return new ChosenItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChosenItemViewHolder holder, int position) {
        ChosenItem p = items.get(position);
        holder.textViewItemName.setText(p.getProduct().getName());
        holder.textViewMerchantName.setText(p.getProduct().getMerchant().getName());
        holder.textViewQuantity.setText(p.getQuantity());
        if (p.getProduct().getProductSize().get(0) != null) {
            holder.textViewSize.setText(p.getSize());
            holder.textViewPrice.setText(p.getProduct().getPrice().get(p.getProduct().getProductSize().indexOf(holder.textViewSize.getText())));
        }
        else {
            holder.textViewPrice.setText(p.getProduct().getPrice().get(0));
        }

        String basic_price = holder.textViewPrice.getText().toString();
        Log.d("total price", CartActivity.totalPrice);
        CartActivity.totalPrice = (Integer.parseInt(CartActivity.totalPrice) +
                Integer.parseInt(basic_price) * Integer.parseInt(p.getQuantity())) + "";
        CartActivity.updateTotalPriceAndCost();

        holder.textViewPrice.setText((Integer.parseInt(basic_price) * Integer.parseInt(holder.textViewQuantity.getText().toString())) + "");
        holder.imageViewItem.setImageDrawable(null);
        Glide.with(context).load(p.getProduct().getImage().get(0)).into(holder.imageViewItem);
        holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p.getProduct().getImage().size() <= 1) {
                    p.getProduct().getFullListProductImage(new OnGetDataListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(context, ProductActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("Product", (Parcelable) p.getProduct());
                            if (LoginActivity.firebase.favouriteProductList.contains(p.getProduct().getId()))
                                intent.putExtra("IsFavourite", true);
                            else intent.putExtra("IsFavourite", false);
                            context.startActivity(intent);
                        }
                    });
                }
                else {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Product", (Parcelable) p.getProduct());
                    if (LoginActivity.firebase.favouriteProductList.contains(p.getProduct().getId()))
                        intent.putExtra("IsFavourite", true);
                    else intent.putExtra("IsFavourite", false);
                    context.startActivity(intent);
                }
            }
        });
        holder.buttonAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textViewQuantity.setText((Integer.parseInt(holder.textViewQuantity.getText().toString()) + 1) + "");
                p.setQuantity((Integer.parseInt(p.getQuantity()) + 1) + "");
                holder.textViewPrice.setText((Integer.parseInt((String) holder.textViewQuantity.getText()) *
                        Integer.parseInt(basic_price)) + "");
                LoginActivity.firebase.updateProductQuantityInCart(p);

                CartActivity.totalPrice = (Integer.parseInt(CartActivity.totalPrice) + Integer.parseInt(basic_price)) + "";
                CartActivity.updateTotalPriceAndCost();
            }
        });
        holder.buttonRemoveQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textViewQuantity.getText().equals("1")) {

                }
                else {
                    holder.textViewQuantity.setText((Integer.parseInt(holder.textViewQuantity.getText().toString()) - 1) + "");
                    p.setQuantity((Integer.parseInt(p.getQuantity()) - 1) + "");
                    holder.textViewPrice.setText((Integer.parseInt((String) holder.textViewQuantity.getText()) *
                            Integer.parseInt(basic_price)) + "");
                    LoginActivity.firebase.updateProductQuantityInCart(p);

                    CartActivity.totalPrice = (Integer.parseInt(CartActivity.totalPrice) - Integer.parseInt(basic_price)) + "";
                    CartActivity.updateTotalPriceAndCost();
                    if (!CartActivity.checkVoucherUtilization(CartActivity.voucherMinSumPrice)) {
                        CartActivity.cancelVoucher();
                    }
                }
            }
        });
        holder.buttonRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.firebase.cartList.remove(p);
                CartActivity.chosenItemAdapter.notifyDataSetChanged();
                CartActivity.totalPrice = "0";
                CartActivity.textViewDistance.setVisibility(View.INVISIBLE);
                LoginActivity.firebase.removeProductFromCartWithContext(p, context);
                CartActivity.updateTotalPriceAndCost();
                CartActivity.getMerchantDistance();
                if (!CartActivity.checkVoucherUtilization(CartActivity.voucherMinSumPrice)) {
                    CartActivity.cancelVoucher();
                }
                HomeFragment.updateCartBadge();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ChosenItemViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewProduct;
        ImageView imageViewItem;
        ImageButton buttonAddQuantity, buttonRemoveQuantity, buttonRemoveItem;
        TextView textViewItemName, textViewSize, textViewPrice, textViewQuantity, textViewMerchantName;

        public ChosenItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewProduct = itemView.findViewById(R.id.cardview_product);
            textViewPrice = itemView.findViewById(R.id.tv_item_price);
            textViewSize = itemView.findViewById(R.id.tv_size);
            textViewItemName = itemView.findViewById(R.id.tv_name);
            imageViewItem = itemView.findViewById(R.id.img_item);
            textViewQuantity = itemView.findViewById(R.id.tv_quantity);
            textViewMerchantName = itemView.findViewById(R.id.tv_merchant_name);
            buttonAddQuantity = itemView.findViewById(R.id.btn_add_quantity);
            buttonRemoveQuantity = itemView.findViewById(R.id.btn_remove_quantity);
            buttonRemoveItem = itemView.findViewById(R.id.btn_remove_item);
        }
    }
}
