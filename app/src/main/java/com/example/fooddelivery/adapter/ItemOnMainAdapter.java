package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.activity.MainActivity;
import com.example.fooddelivery.activity.ProductActivity;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Merchant;
import com.example.fooddelivery.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;

public class ItemOnMainAdapter extends RecyclerView.Adapter<ItemOnMainAdapter.ItemOnMainViewHolder> {

    private final Context context;
    private final List<Product> items;
    private Merchant itemMerchant;

    public ItemOnMainAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = items;
        this.itemMerchant = new Merchant();
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
        Glide.with(context).load(p.getImage().get(0)).into(holder.imageViewItem);
//        holder.imageViewItem.setImageResource(p.getImage_id());
        holder.textViewItemPrice.setText(p.getPrice().get(1) + " đ");
        holder.textViewItemName.setText(p.getName());
        holder.textViewRating.setText(p.getRating());
        holder.textViewItemMerchant.setText(p.getMerchant());
        holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMerchantNameFromIdAndNavigate(p);
            }
        });
    }

    public void getMerchantNameFromIdAndNavigate(Product p) {
        FirebaseFirestore.getInstance().document("Merchant/" + p.getMerchant())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        itemMerchant = new Merchant(((String) documentSnapshot.get("Name")),
                                ((String) documentSnapshot.get("Address")));

                        Intent intent = new Intent(context, ProductActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("ClickedProductMerchant", itemMerchant);
                        intent.putExtra("ClickedProduct", p);
                        context.startActivity(intent);
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

        public ItemOnMainViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemPrice = itemView.findViewById(R.id.tv_item_price);
            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            textViewItemMerchant = itemView.findViewById(R.id.tv_item_merchant);
            imageViewItem = itemView.findViewById(R.id.img_item);
            imageViewLove = itemView.findViewById(R.id.ic_love_item);
            textViewRating = itemView.findViewById(R.id.tv_rating);
            cardViewProduct = itemView.findViewById(R.id.cardview_product);

            imageViewLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            });
        }
    }
}
