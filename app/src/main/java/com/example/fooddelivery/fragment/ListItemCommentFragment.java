package com.example.fooddelivery.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.RatingOrderItemsAdapter;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.Product;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;

public class ListItemCommentFragment extends Fragment {

    ArrayList<OrderItem> listOrderItem = new ArrayList<>();
    ListView lv_Commented;
    RatingOrderItemsAdapter adapter;
    ImageButton bt_Back;

    public ListItemCommentFragment(ArrayList<OrderItem> listOrderItem) {
        this.listOrderItem = listOrderItem;
    }

    public ListItemCommentFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_item_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                getFragmentManager().popBackStack();
            }
        });
    }

    void initView() {
        lv_Commented= (ListView) getView().findViewById(R.id.lv_fm_ordcmt);
        adapter = new RatingOrderItemsAdapter(getActivity(), listOrderItem, this.getFragmentManager());
        lv_Commented.setAdapter(adapter);

        bt_Back = (ImageButton)getView().findViewById(R.id.bt_fm_ordcmt_back);
    }

//    boolean refresh = false;
//    @Override
//    public void onPause() {
//        super.onPause();
//        refresh = true;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (refresh)
//            listOrderItem = LoginActivity.firebase.getListOrderedItems(listOrderItem.get(0).getOrder_id());
//
//        Log.e("listOrderItem", listOrderItem.get(0).getComment().getDetails());
//    }
}
