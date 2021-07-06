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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.adapter.NotificationAdapter;
import com.example.fooddelivery.model.MyNotification;
import com.example.fooddelivery.model.OnGetDataListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    List<MyNotification> notifications;
    RecyclerView recyclerView;
    private static NotificationAdapter mAdapter = null;
    public NotificationFragment() {
        this.notifications = WelcomeActivity.firebase.notifications;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView)getView().findViewById(R.id.notification_recycler);
        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(), notifications);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(notificationAdapter);
        mAdapter = notificationAdapter;
    }

    public static NotificationAdapter getAdapter()
    {
        return mAdapter;
    }
}
