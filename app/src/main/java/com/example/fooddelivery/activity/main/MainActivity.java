package com.example.fooddelivery.activity.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.ItemOnMainAdapter;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.fragment.MeFragment;
import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.NotificationFragment;
import com.example.fooddelivery.fragment.OrderFragment;
import com.example.fooddelivery.model.CallBackData;
import com.example.fooddelivery.model.ModifyFirebase;
import com.example.fooddelivery.model.MyNotification;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNav;

    boolean doubleBackToExitPressedOnce = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getText(R.string.press_back_twice_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void initView () {
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);
        initBottomNavigation();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private void getData() {
        LoginActivity.firebase.getVoucherList();
        LoginActivity.firebase.getAvailableVoucherList();
        LoginActivity.firebase.getComment();
        LoginActivity.firebase.getUserInfo();
        LoginActivity.firebase.getNotificationList(new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess() {
                for (MyNotification notification : LoginActivity.firebase.notifications)
                {
                    if (notification.getStatus().equals("false"))
                    {
                        sendNotification(notification.getTitle(), notification.getDesc());
                        LoginActivity.firebase.updateNotificationStatus(notification.getId());
                    }
                }
            }
        });
    }

    private void initBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        temp = new HomeFragment();
                        break;
                    case R.id.nav_order:
                        temp = new OrderFragment(MainActivity.this, returnLanguage());
                        break;
                    case R.id.nav_me:
                        temp = new MeFragment();
                        break;
                    case R.id.nav_notification:
                        temp = new NotificationFragment();
                        break;
                    }

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.fragment_container, temp);
                ft.commit();
                return true;
            }
        });
    }

    String returnLanguage()
    {
        String str = getString(R.string.ic_home);
        if (str.equals("Home"))
            return "en";
        return "vi";
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendNotification(String title, String message)
    {
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