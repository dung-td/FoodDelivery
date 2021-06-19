package com.example.fooddelivery.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.GeneralOrderAdapter;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.OrderStatus;
import com.example.fooddelivery.model.Orders;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.ProductStatus;

import java.util.ArrayList;

public class GeneralOrdersFragment extends Fragment {

    ListView lv_OrdersList;
    ArrayList<Orders> listOrders = new ArrayList<>();

    public GeneralOrdersFragment() {

    }

    public  GeneralOrdersFragment(ArrayList<Orders> listOrders) {
        this.listOrders = listOrders;
        if (listOrders.size() == 0)
            Log.e("General Orders Fragment", " size: 0");
        else
            Log.e("General Orders Fragment", Integer.toString(listOrders.size()));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_orders, container, false);
        lv_OrdersList= (ListView) v.findViewById(R.id.lv_fm_listorders);
        GeneralOrderAdapter adapter = new GeneralOrderAdapter(getActivity(), listOrders);
        lv_OrdersList.setAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv_OrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderDetailsFragment nextFrag= new OrderDetailsFragment(listOrders.get(position));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }


}
