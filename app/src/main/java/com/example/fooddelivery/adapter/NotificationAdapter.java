package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.NotificationDetailActivity;
import com.example.fooddelivery.model.MyNotification;

import java.util.List;
import java.util.Locale;

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
        loadLanguage();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        MyNotification n = notifications.get(position);
        String langPref = "lang_code";
        SharedPreferences prefs = context.getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        if (language.equals("vi")) {
            holder.tvTitle.setText(n.getTitle_vn());
            holder.tvDesc.setText(n.getDesc_vn());
        }
        else {
            holder.tvTitle.setText(n.getTitle_en());
            holder.tvDesc.setText(n.getDesc_en());
        }
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

    void loadLanguage() {
        String langPref = "lang_code";
        SharedPreferences prefs = context.getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");

        Locale locale = new Locale(language);

        Log.e("MainActivity language", language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
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
