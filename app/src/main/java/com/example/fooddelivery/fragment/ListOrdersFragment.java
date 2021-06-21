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
import com.example.fooddelivery.adapter.OrderProductAdapter;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.Orders;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView lv_OrdersList;
    ArrayList<Orders> listOrders = new ArrayList<>();

    public ListOrdersFragment() {
        // Required empty public constructor
        Add();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOrdersFragment newInstance(String param1, String param2) {
        ListOrdersFragment fragment = new ListOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_orders, container, false);
        lv_OrdersList= (ListView) v.findViewById(R.id.lv_fm_listorders);
        OrderProductAdapter adapter = new OrderProductAdapter(getActivity(), listOrders);
        lv_OrdersList.setAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        lv_OrdersList= (ListView) getView().findViewById(R.id.lv_fm_listorders);
//        OrderProductAdapter adapter = new OrderProductAdapter(getActivity(), listOrders);
//        lv_OrdersList.setAdapter(adapter);
//        Log.e("Load", "Load");
//        lv_OrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("Click", "Click");
//                OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
//                FragmentManager fmgr = getFragmentManager();
//
//                FragmentTransaction ft = fmgr.beginTransaction();
//
//                ft.replace(R.id.fragment_container, orderDetailsFragment);
//
//                ft.commit();
        //        }
        //   });
    }

    @Override
    public void onStart() {
        super.onStart();
        lv_OrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Click", "Click");
                OrderDetailsFragment nextFrag= new OrderDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    void Add()
    {
        ArrayList<OrderItem> listOrderItems = new ArrayList<>() ;
        Product product = new Product();
//        listOrderItems.add(new OrderItem(1, product, 2));
//        listOrderItems.add(new OrderItem(1, product, 2));

//        Orders orders = new Orders(1, 1, "abc", "1/1/2020", listOrderItems);

//        listOrders.add(orders);
//        listOrders.add(orders);
//        listOrders.add(orders);
    }
}
