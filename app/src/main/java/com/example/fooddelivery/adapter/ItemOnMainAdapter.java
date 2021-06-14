package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.activity.ProductActivity;
import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.model.Product;

import java.util.List;

public class ItemOnMainAdapter extends RecyclerView.Adapter<ItemOnMainAdapter.ItemOnMainViewHolder> {

    private final Context context;
    private final List<Product> items;

    public ItemOnMainAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemOnMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.watched_item_layout, parent, false);
        return new ItemOnMainViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemOnMainViewHolder holder, int position) {
        Product p = items.get(position);
        if (LoginActivity.firebase.favouriteProductList.contains(p.getId())) {
            holder.isFavourite = true;
            holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else {
            holder.isFavourite = false;
            holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        holder.textViewItemPrice.setText(p.getPrice().get(0) + " đ");
        holder.textViewItemName.setText(p.getName());
        holder.textViewRating.setText(p.getRating());
        holder.textViewItemMerchant.setText(p.getMerchant().getName());
        Glide.with(context).load(p.getImage().get(0)).into(holder.imageViewItem);
        holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Product", (Parcelable) p);
                intent.putExtra("IsFavourite", holder.isFavourite);
                context.startActivity(intent);
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
                }
                else {
                    holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_24);
                    LoginActivity.firebase.favouriteProductList.add(p.getId());
                    LoginActivity.firebase.addProductToFavourite(context, p.getId());
                    holder.isFavourite = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items.size() != 0) {
            return items.size();
        }
        else return 0;
    }

    public class ItemOnMainViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewProduct;
        ImageView imageViewItem, imageViewLove;
        TextView textViewItemName, textViewItemMerchant, textViewItemPrice, textViewRating;
        boolean isFavourite = false;

        public ItemOnMainViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemPrice = itemView.findViewById(R.id.tv_item_price);
            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            textViewItemMerchant = itemView.findViewById(R.id.tv_item_merchant);
            imageViewItem = itemView.findViewById(R.id.img_item);
            imageViewLove = itemView.findViewById(R.id.ic_love_item);
            textViewRating = itemView.findViewById(R.id.tv_rating);
            cardViewProduct = itemView.findViewById(R.id.cardview_product);
        }
    }
}
