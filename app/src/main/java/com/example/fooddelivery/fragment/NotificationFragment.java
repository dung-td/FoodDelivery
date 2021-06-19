package com.example.fooddelivery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.adapter.NotificationAdapter;
import com.example.fooddelivery.model.Notification;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.ProductStatus;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    List<Notification> notifications;

    public NotificationFragment() {
        notifications = new ArrayList<>();
        notifications.add(new Notification(1, "Phở", "Done", "19/1/1341"));
        notifications.add(new Notification(1, "Phở", "Done", "19/1/1341"));
        notifications.add(new Notification(1, "Phở", "Done", "19/1/1341"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.notification_recycler);
        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(), notifications);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
    }
}
