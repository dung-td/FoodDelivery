package com.example.fooddelivery.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fooddelivery.adapter.ItemOnMainAdapter;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.fragment.MeFragment;
import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.OrderFragment;
import com.example.fooddelivery.model.Merchant;
import com.example.fooddelivery.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<Product> productList;
    public static FirebaseFirestore root = FirebaseFirestore.getInstance();
    BottomNavigationView bottomNav;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirebaseData();
    }

    private void initFirebaseData() {
        productList = new ArrayList<Product>();
        root.collection("Product/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            Product product = new Product();
                            product.setId((String) document.getId());
                            product.setName((String) document.get("Name"));
                            product.setStatus((String) document.get("Status"));
                            product.setPrice((ArrayList<String>) document.get("Price"));
                            product.setProductSize((ArrayList<String>) document.get("Size"));
                            product.setMerchant(splitMerchantId((String) document.get("Merchant")));
                            product.setRating((String) document.get("Rating"));
                            getImageList(product);
                        }
                        initView();
                    }
                });
    }

    private String splitMerchantId(String data) {
        return data.substring(9);
    }

    private void getImageList(Product p) {
        ArrayList<Uri> images = new ArrayList<Uri>();
        root.collection("Product/" + p.getId() + "/Photos/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            images.add(Uri.parse((String) document.get("Image_Link")));
                        }
                        p.setImage(images);
                        productList.add(p);
                    }
                });
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
                            temp = new OrderFragment();
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

}