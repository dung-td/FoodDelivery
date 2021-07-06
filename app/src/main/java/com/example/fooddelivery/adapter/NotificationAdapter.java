package com.example.fooddelivery.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.NotificationDetailActivity;
import com.example.fooddelivery.model.MyNotification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private final Context context;
    private final List<MyNotification> notifications;

    public NotificationAdapter(Context context, List<MyNotification> notifications) {
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
        MyNotification n = notifications.get(position);
        holder.tvTitle.setText(n.getTitle());
        holder.tvDesc.setText(n.getDesc());
        holder.tvTime.setText(n.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotificationDetailActivity.class);
                //intent.putExtra("Title", n.getTitle());
                intent.putExtra("Notification", (Parcelable) n);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // return products.size();
        return notifications != null ? notifications.size() : 0;
    }

    public static final class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvTime;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.notification_title);
            tvDesc = itemView.findViewById(R.id.notification_description);
            tvTime = itemView.findViewById(R.id.notification_time);
        }
    }
}
