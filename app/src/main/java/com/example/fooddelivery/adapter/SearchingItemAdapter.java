package com.example.fooddelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.main.SearchingActivity;

import java.util.List;

public class SearchingItemAdapter extends RecyclerView.Adapter<SearchingItemAdapter.SearchingItemViewHolder> {

    private final Context context;
    private final List<String> items;

    public SearchingItemAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SearchingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recent_search_item_layout, parent, false);
        return new SearchingItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchingItemViewHolder holder, int position) {
        String input = items.get(position);
        holder.textViewSearchInput.setText(input);
        holder.buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchingActivity) context).setSearchInputFromUp(input);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class SearchingItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSearchInput;
        ImageButton buttonUp;

        public SearchingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSearchInput = itemView.findViewById(R.id.tv_search);
            buttonUp = itemView.findViewById(R.id.btn_up);
        }
    }

}
