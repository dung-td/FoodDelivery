package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Product;

import java.util.List;

public class ProductOnSectionAdapter extends RecyclerView.Adapter<ProductOnSectionAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> products;

    public ProductOnSectionAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductOnSectionAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_on_section_layout, parent, false);
        return new ProductOnSectionAdapter.ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductOnSectionAdapter.ProductViewHolder holder, int position) {
        Product p = products.get(position);
        holder.textViewName.setText(p.getName());
        Glide.with(context).load(p.getImage()).into(holder.imageViewProduct);
        holder.textViewPrice.setText(p.getPrice() + " đ");
        holder.textViewRating.setText(p.getRating() + "");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProduct;
        TextView textViewName, textViewPrice, textViewRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_item_name_merchant);
            textViewPrice = itemView.findViewById(R.id.tv_item_price);
            imageViewProduct = itemView.findViewById(R.id.img_item);
            textViewRating = itemView.findViewById(R.id.tv_rating);
        }
    }
}
