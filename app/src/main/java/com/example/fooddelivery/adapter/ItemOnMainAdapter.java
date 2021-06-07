package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.activity.ProductActivity;
import com.example.fooddelivery.R;
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
        //holder.imageViewItem.setImageResource(p.getImage_id());
        holder.textViewItemPrice.setText(p.getPrice() + " đ");
        holder.textViewItemName.setText(p.getName());
        holder.textViewRating.setText(p.getRating());
        //holder.textViewItemMerchant.setText(p.getMerchant_name());
    }

    @Override
    public int getItemCount() {
        return items.size();
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

            cardViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
