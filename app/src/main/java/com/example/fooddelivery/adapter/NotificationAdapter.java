package com.example.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Notification;
import com.example.fooddelivery.model.Product;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    Context context;
    List<Notification> notifications;

    public NotificationAdapter(){}

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification n = notifications.get(position);
        holder.tvTitle.setText(n.getTitle());
        holder.tvDesc.setText(n.getDesc());
    }

    @Override
    public int getItemCount() {
        // return products.size();
        return notifications != null ? notifications.size() : 0;
    }

    public static final class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.notification_title);
            tvDesc = itemView.findViewById(R.id.notification_description);
        }
    }
}
