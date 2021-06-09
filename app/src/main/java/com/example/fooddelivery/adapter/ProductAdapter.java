package com.example.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
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
        holder.textViewNameVn.setText(p.getName());
        holder.imageViewProduct.setImageURI(p.getImage().get(0));
        holder.textViewPrice.setText(p.getPrice() + " đ");

        //init spinner values
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(context, R.array.product_size, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerProductSize.setAdapter(staticAdapter);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProduct;
        TextView textViewNameVn, textViewPrice;
        Spinner spinnerProductSize;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameVn = itemView.findViewById(R.id.tv_item_name_vn);
            textViewPrice = itemView.findViewById(R.id.tv_item_price);
            imageViewProduct = itemView.findViewById(R.id.img_item);
            spinnerProductSize = itemView.findViewById(R.id.spinner_size);
        }
    }
}
