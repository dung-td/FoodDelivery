package com.example.fooddelivery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.ProductAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {

    List<Product> productList;

    public ItemFragment() {
        productList = new ArrayList<>();
        productList = LoginActivity.firebase.productList;
        // Required empty public constructor
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
//        getMerchantItems();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ProductAdapter productAdapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);
    }
}
