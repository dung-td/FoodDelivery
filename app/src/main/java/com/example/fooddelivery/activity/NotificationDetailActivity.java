package com.example.fooddelivery.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.MyNotification;

public class NotificationDetailActivity extends AppCompatActivity {
    TextView title;
    TextView description;
    TextView time;
    ImageView buttonBack;
    MyNotification notification;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
    }

    private void initView()
    {
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        time = findViewById(R.id.time);
        buttonBack = findViewById(R.id.ic_back_arrow);
        notification = (MyNotification)getIntent().getParcelableExtra("Notification");
//
        title.setText(notification.getTitle());
        description.setText(notification.getDesc());
        time.setText(notification.getTime());

        this.notificationManagerCompat = NotificationManagerCompat.from(this);
        //Back button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                sendNotification();
                NotificationDetailActivity.super.onBackPressed();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendNotification()
    {
        String title = this.title.getText().toString();
        String message = this.description.getText().toString();

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_add_img)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel1",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}