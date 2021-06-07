package com.example.fooddelivery.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.adapter.RatedOrderAdapter;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCommentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<OrderItem> listOrderItem = new ArrayList<>();
    ListView lv_Commented;
    Button bt_Back;

    public OrderCommentFragment() {
        // Required empty public constructor
        Add();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderCommentFragment newInstance(String param1, String param2) {
        OrderCommentFragment fragment = new OrderCommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_Commented= (ListView) getView().findViewById(R.id.lv_fm_ordcmt);
        RatedOrderAdapter adapter = new RatedOrderAdapter(getActivity(), listOrderItem);
        lv_Commented.setAdapter(adapter);

        ImageButton bt_Back = (ImageButton)getView().findViewById(R.id.bt_fm_ordcmt_back);
        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    void Add()
    {
        Product drink = new Product();
        Product pho = new Product();
        OrderItem itemPho =new OrderItem(1, pho , 2);
        OrderItem itemDrink = new OrderItem(2, drink, 1);

        listOrderItem.add(itemPho);
        listOrderItem.add(itemDrink);
    }
}
