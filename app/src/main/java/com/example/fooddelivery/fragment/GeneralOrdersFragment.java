package com.example.fooddelivery.fragment;

import android.app.ProgressDialog;
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
import com.example.fooddelivery.adapter.ViewPagerOrdersAdapter;
import com.example.fooddelivery.model.OnGetDataListener;

public class GeneralOrdersFragment extends Fragment {

    ProgressDialog progressDialog;
    int Position;
    ListView lv_OrdersList;
    GeneralOrderAdapter adapter;

    public GeneralOrdersFragment() {

    }

    public GeneralOrdersFragment(int Position) {
        this.Position = Position;
        Log.e("New Fragment", "Test: " + Position);
        if (Position == 0)
            getOrderChangeListener();

    }

    private void getOrderChangeListener() {
        LoginActivity.firebase.OrderStatusChange(new OnGetDataListener() {
            @Override
            public void onStart() {
                // progressDialog.setMessage(getString(R.string.data_loading));
                // progressDialog.show();
            }

            @Override
            public void onSuccess() {
                if (GeneralOrderAdapter.listOrders == null)
                    return;

                if (GeneralOrderAdapter.listOrders.size() == 0)
                    return;

                ViewPagerOrdersAdapter.adapter0.adapter.notifyDataSetChanged();
                ViewPagerOrdersAdapter.adapter1.adapter.notifyDataSetChanged();
                ViewPagerOrdersAdapter.adapter2.adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_orders, container, false);
        lv_OrdersList = (ListView) v.findViewById(R.id.lv_fm_listorders);
        adapter = new GeneralOrderAdapter(getActivity(), Position);
        lv_OrdersList.setAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv_OrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderDetailsFragment nextFrag = new OrderDetailsFragment(
                        LoginActivity.firebase.orderList.get(Position).get(position));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
