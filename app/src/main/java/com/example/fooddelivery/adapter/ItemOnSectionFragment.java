package com.example.fooddelivery.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemOnSectionFragment extends Fragment {

    private static List<Product> productList;
    int productListType = 0;

    public ItemOnSectionFragment(int listType) {
        productList = new ArrayList<>();
        productListType = listType;

        //Toàn bộ drink
        for (Product p : WelcomeActivity.firebase.productList) {
            if (p.getType().equals("Drink"))
                productList.add(p);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_item);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ProductOnSectionAdapter productAdapter;
        if (productListType == 0) {
            productAdapter = new ProductOnSectionAdapter(getContext(), productList);
            recyclerView.setAdapter(productAdapter);
        }
        else {
            //Drink được đánh giá cao
            if (productListType == 1) {
                ArrayList<Product> list = new ArrayList<>();
                for (Product p : productList) {
                    if (Float.parseFloat(p.getRating()) > 4.6)
                        list.add(p);
                }
                productAdapter = new ProductOnSectionAdapter(getContext(), list);
                recyclerView.setAdapter(productAdapter);
            }
        }
    }
}
