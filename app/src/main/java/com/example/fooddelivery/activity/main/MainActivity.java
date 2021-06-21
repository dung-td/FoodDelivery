package com.example.fooddelivery.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private static final int PLACE_PICKER_REQUEST = 1;
    FloatingActionButton bt_location;
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
        bt_location = findViewById(R.id.locationButton);
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);
        initBottomNavigation();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder builder = new StringBuilder();
                String latitute = String.valueOf(place.getLatLng().latitude);
                String longtitue = String.valueOf(place.getLatLng().longitude);
            }
        }
    }

    private void getData() {
        LoginActivity.firebase.getVoucherList();
        LoginActivity.firebase.getAvailableVoucherList();
        LoginActivity.firebase.getComment();
        LoginActivity.firebase.getUserInfo();
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
}