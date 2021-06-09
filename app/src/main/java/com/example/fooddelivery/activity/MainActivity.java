package com.example.fooddelivery.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.fragment.MeFragment;
import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView () {
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
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
//                        case R.id.nav_notification:
//                            temp = new NotificationFragment();
//                            break;
                        case R.id.nav_me:
                            temp = new MeFragment();
                            break;
                    }

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.fragment_container, temp);
                    ft.commit();
                    return true;
                }
            };

    String returnLanguage()
    {
        String str = getString(R.string.ic_home);
        if (str.equals("Home"))
            return "en";
        return "vi";
    }


}