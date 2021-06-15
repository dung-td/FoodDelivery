package com.example.fooddelivery.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.activity.main.CartActivity;
import com.example.fooddelivery.activity.main.DrinkSectionActivity;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.main.FavouriteSectionActivity;
import com.example.fooddelivery.activity.main.WatchedSectionActivity;
import com.example.fooddelivery.adapter.ItemOnMainAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.GridSpacingItemDecoration;
import com.example.fooddelivery.model.OnGetDataListener;

public class HomeFragment extends Fragment {

    LinearLayout sectionDrink, sectionFavourite, sectionWatched;
    FrameLayout cartBackground;
    boolean isFirstClick = true;
    public static TextView cartBadge;
    public static ItemOnMainAdapter itemOnMainAdapter;
    public static RecyclerView recyclerViewProducts;

    public HomeFragment() {
    }

    private void initClickListener() {
        sectionDrink = getView().findViewById(R.id.section_drink);
        sectionFavourite = getView().findViewById(R.id.section_love);
        sectionWatched = getView().findViewById(R.id.section_watched);
        cartBackground = getView().findViewById(R.id.btn_cart_background);
        sectionDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DrinkSectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        sectionFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FavouriteSectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        sectionWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstClick) {
                    isFirstClick = false;
                    LoginActivity.firebase.getWatchedProductList(new OnGetDataListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(getContext(), WatchedSectionActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
                else {
                    Intent intent = new Intent(getContext(), WatchedSectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        cartBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        cartBadge = getView().findViewById(R.id.cart_badge);
        //updateCartBadge();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClickListener();

        recyclerViewProducts = (RecyclerView)getView().findViewById(R.id.recycler_view_products);
        recyclerViewProducts.setHasFixedSize(true);

     //   itemOnMainAdapter = new ItemOnMainAdapter(getContext(), LoginActivity.firebase.productList);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        int spanCount = 2;
        int spacing = 40;
        boolean includeEdge = true;
        recyclerViewProducts.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerViewProducts.setLayoutManager(manager);
        recyclerViewProducts.setAdapter(itemOnMainAdapter);
    }

    public static void updateCartBadge() {
        if (LoginActivity.firebase.cartList != null && LoginActivity.firebase.cartList.size() > 0) {
            cartBadge.setText(LoginActivity.firebase.cartList.size() + "");
            cartBadge.getBackground().setTint(Color.parseColor("#57BFFF"));
        }
    }
}

