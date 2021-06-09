package com.example.fooddelivery.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.activity.MainActivity;
import com.example.fooddelivery.adapter.ItemOnMainAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.GridSpacingItemDecoration;
import com.example.fooddelivery.model.ModifyFirebase;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<Product> products;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        products = new ArrayList<>();
        products = MainActivity.productList;

        RecyclerView recyclerViewProducts = (RecyclerView)getView().findViewById(R.id.recycler_view_products);
        recyclerViewProducts.setHasFixedSize(true);

        ItemOnMainAdapter itemOnMainAdapter = new ItemOnMainAdapter(getContext(), products);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        int spanCount = 2; // 3 columns
        int spacing = 40; // 50px
        boolean includeEdge = true;
        recyclerViewProducts.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerViewProducts.setLayoutManager(manager);
        recyclerViewProducts.setAdapter(itemOnMainAdapter);
    }
}

