package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.activity.main.ProductActivity;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.OnGetDataListener;
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
        if (WelcomeActivity.firebase.favouriteProductList.contains(p.getId())) {
            holder.isFavourite = true;
            holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else {
            holder.isFavourite = false;
            holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
       // holder.textViewName.setText(p.getName() + " - " + p.getMerchant().getName());
        setNameItem(holder, p);
        holder.imageViewProduct.setImageDrawable(null);
        Glide.with(context).load(p.getImage().get(0)).into(holder.imageViewProduct);
        holder.textViewPrice.setText(p.getPrice().get(0) + " d");
        holder.textViewRating.setText(p.getRating());
        holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p.getImage().size() <= 1) {
                    p.getFullListProductImage(new OnGetDataListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(context, ProductActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("Product", (Parcelable) p);
                            intent.putExtra("IsFavourite", holder.isFavourite);
                            context.startActivity(intent);
                        }
                    });
                }
                else {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Product", (Parcelable) p);
                    intent.putExtra("IsFavourite", holder.isFavourite);
                    context.startActivity(intent);
                }
            }
        });
        holder.imageViewLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WelcomeActivity.firebase.favouriteProductList.contains(p.getId())) {
                    holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    WelcomeActivity.firebase.favouriteProductList.remove(p.getId());
                    WelcomeActivity.firebase.removeProductFromFavourite(context, p.getId());
                    holder.isFavourite = false;
                    HomeFragment.itemOnMainAdapter.notifyDataSetChanged();
                }
                else {
                    holder.imageViewLove.setImageResource(R.drawable.ic_baseline_favorite_24);
                    WelcomeActivity.firebase.favouriteProductList.add(p.getId());
                    WelcomeActivity.firebase.addProductToFavourite(context, p.getId());
                    holder.isFavourite = true;
                    HomeFragment.itemOnMainAdapter.notifyDataSetChanged();
                }
            }
        });

        if (p.getProductSize().get(0) != null) {
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
        }
        else {
            holder.spinnerProductSize.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProduct, imageViewLove;
        TextView textViewName, textViewPrice, textViewRating;
        Spinner spinnerProductSize;
        CardView cardViewProduct;
        boolean isFavourite = false;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_item_name_merchant);
            textViewPrice = itemView.findViewById(R.id.tv_item_price);
            imageViewProduct = itemView.findViewById(R.id.img_item);
            imageViewLove = itemView.findViewById(R.id.ic_love_item);
            textViewRating = itemView.findViewById(R.id.tv_rating);
            spinnerProductSize = itemView.findViewById(R.id.spinner_size);
            cardViewProduct = itemView.findViewById(R.id.cardview_product);
        }
    }

    void setNameItem(ProductOnSectionAdapter.ProductViewHolder holder, Product p)
    {
        Log.e("Welcome languge", WelcomeActivity.language);
        if (WelcomeActivity.language.equals("vi"))
            holder.textViewName.setText(p.getName());
        else
            holder.textViewName.setText(p.getEn_Name());
    }
}
