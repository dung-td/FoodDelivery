package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.MerchantActivity;
import com.example.fooddelivery.activity.ProductActivity;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = products.get(position);
        if (LoginActivity.firebase.favouriteProductList.contains(p.getId())) {
            holder.isFavourite = true;
            holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else {
            holder.isFavourite = false;
            holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
        holder.textViewNameVn.setText(p.getName());
        holder.textViewNameEn.setText(p.getEn_Name());

        Glide.with(context).load(p.getImage().get(0)).into(holder.imageViewProduct);
        holder.textViewPrice.setText(p.getPrice().get(0) + " d");
        holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProductActivity.fa.finish();
                Intent intent = new Intent(context, ProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("IsFavourite", holder.isFavourite);
                intent.putExtra("Product", (Parcelable) p);
                context.startActivity(intent);
            }
        });

        ArrayAdapter<String> staticAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, p.getProductSize());
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerProductSize.setAdapter(staticAdapter);

        holder.spinnerProductSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.textViewPrice.setText(p.getPrice().get(position) + " d");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.imageViewLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.firebase.favouriteProductList.contains(p.getId())) {
                    holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    LoginActivity.firebase.favouriteProductList.remove(p.getId());
                    LoginActivity.firebase.removeProductFromFavourite(context, p.getId());
                    holder.isFavourite = false;
                    HomeFragment.itemOnMainAdapter.notifyDataSetChanged();
                }
                else {
                    holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_24);
                    LoginActivity.firebase.favouriteProductList.add(p.getId());
                    LoginActivity.firebase.addProductToFavourite(context, p.getId());
                    holder.isFavourite = true;
                    HomeFragment.itemOnMainAdapter.notifyDataSetChanged();
                }
            }
        });
        holder.buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.firebase.cartList.add(p);
                MerchantActivity.updateCart();
                HomeFragment.updateCartBadge();
                ProductActivity.updateCartBadge();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProduct, imageViewLove;
        ImageButton buttonAddItem;
        TextView textViewNameVn, textViewNameEn, textViewPrice;
        Spinner spinnerProductSize;
        CardView cardViewProduct;
        boolean isFavourite = false;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameVn = itemView.findViewById(R.id.tv_item_name_vn);
            textViewNameEn = itemView.findViewById(R.id.tv_item_name_en);
            imageViewLove = itemView.findViewById(R.id.ic_love_item);
            textViewPrice = itemView.findViewById(R.id.tv_item_price);
            imageViewProduct = itemView.findViewById(R.id.img_item);
            buttonAddItem = itemView.findViewById(R.id.btn_add_item);
            cardViewProduct = itemView.findViewById(R.id.cardview_product);
            spinnerProductSize = itemView.findViewById(R.id.spinner_size);
        }
    }
}
