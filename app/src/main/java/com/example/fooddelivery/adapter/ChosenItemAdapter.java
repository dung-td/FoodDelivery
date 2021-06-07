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

import com.example.fooddelivery.model.ChosenItem;
import com.example.fooddelivery.R;

import java.util.List;

public class ChosenItemAdapter extends RecyclerView.Adapter<ChosenItemAdapter.ChosenItemViewHolder> {

    private final Context context;
    private final List<ChosenItem> items;

    public ChosenItemAdapter(Context context, List<ChosenItem> items) {
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
        holder.imageViewItem.setImageResource(p.getImage_id());
        holder.textViewPrice.setText(p.getItem_price() + " đ");
        holder.textViewItemNameAndQuantity.setText(p.getQuantity() + " x " + p.getItem_name());
        holder.textViewSizeAndNote.setText(p.getItem_size() + ", " + p.getExtra_note());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ChosenItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewItem;
        TextView textViewItemNameAndQuantity, textViewSizeAndNote, textViewPrice;

        public ChosenItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPrice = itemView.findViewById(R.id.tv_item_price);
            textViewSizeAndNote = itemView.findViewById(R.id.tv_size_and_note);
            textViewItemNameAndQuantity = itemView.findViewById(R.id.tv_quantity_and_name);
            imageViewItem = itemView.findViewById(R.id.img_item);
        }
    }
}
