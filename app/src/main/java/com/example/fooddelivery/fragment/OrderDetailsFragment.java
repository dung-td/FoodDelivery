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
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.OrderDetailAdapter;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.Orders;
import com.example.fooddelivery.model.PaymentMethod;
import com.example.fooddelivery.model.User;

import java.util.ArrayList;

public class OrderDetailsFragment extends Fragment {
    TextView tv_nameCustomer ;
    TextView tv_addressCustomer ;
    TextView tv_timeOrdered;
    TextView tv_method ;
    TextView tv_merchant ;
    TextView tv_idOrder;

    ListView lv_OrdersList;
    OrderDetailAdapter adapter;

    TextView tv_tmpPrice;
    TextView tv_shipPrice;
    TextView tv_voucher;
    TextView tv_totalPrice;
    TextView tv_comment;

    Button bt_BuyAgain;
    ImageButton bt_Back ;

    ArrayList<OrderItem> listOrderItem = new ArrayList<>();
    Orders orders;

    public OrderDetailsFragment() {
        // Required empty public constructor

    }

    public OrderDetailsFragment(Orders orders) {
        this.orders=orders;
        this.listOrderItem = orders.getListOrderItems();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFragmentComment();
            }
        });

        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        setData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }



    void OpenFragmentComment() {
        ListItemCommentFragment nextFrag= new ListItemCommentFragment(listOrderItem);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    void initView() {
         tv_nameCustomer = (TextView)getView().findViewById(R.id.tv_fm_orddtail_nameCus);
         tv_addressCustomer = (TextView)getView().findViewById(R.id.tv_fm_orddtail_address);
         tv_timeOrdered = (TextView)getView().findViewById(R.id.tv_fm_orddtail_time);
         tv_method = (TextView)getView().findViewById(R.id.tv_fm_orddtail_method);
         tv_merchant = (TextView)getView().findViewById(R.id.tv_fm_orddtail_merchant);

         lv_OrdersList= (ListView) getView().findViewById(R.id.lv_orderdetails);
         adapter = new OrderDetailAdapter(getActivity(), listOrderItem );
         lv_OrdersList.setAdapter(adapter);

         tv_tmpPrice = (TextView)getView().findViewById(R.id.tv_fm_orddtail_tmpprice);
         tv_shipPrice = (TextView)getView().findViewById(R.id.tv_fm_orddtail_shipprice);
         tv_voucher = (TextView)getView().findViewById(R.id.tv_fm_orddtail_voucher);
         tv_totalPrice = (TextView)getView().findViewById(R.id.tv_fm_orddtail_totalprice);
         tv_comment = (TextView)getView().findViewById(R.id.tv_fm_orddtail_comment);
         tv_idOrder = (TextView)getView().findViewById(R.id.tv_fm_orddtail_ordid);

         //bt_BuyAgain = (Button)getView().findViewById(R.id.bt_fm_orddtail_buy);
         bt_Back = (ImageButton) getView().findViewById(R.id.bt_fm_orddtail_back);

    }

    void setData(){
        User currentUser = LoginActivity.firebase.getUser();
        String.format("%s %s", currentUser.getLast_Name(), currentUser.getFirst_Name());

        tv_nameCustomer.setText(getString(R.string.customer) +": "
                +String.format("%s %s", currentUser.getLast_Name(), currentUser.getFirst_Name()));

        tv_addressCustomer.setText(getString(R.string.address) +": "
                + currentUser.getAddress());

        tv_timeOrdered.setText(getString(R.string.time) +": "+
                orders.getTime().toString());

        if (orders.getMethod().equals(PaymentMethod.COD.toString()))
        tv_method.setText(getString(R.string.payment) + ": " + getString(R.string.cod));


        tv_merchant.setText(orders.getListOrderItems().get(0).getProduct().getMerchant().getName());
        tv_idOrder.setText(getString(R.string.order_id) + ": " + orders.getOrderID());

        tv_tmpPrice.setText(Integer.toString(orders.getTmpPrice()));
        tv_shipPrice.setText(Integer.toString(orders.getFreightCost()));
        tv_voucher.setText(Integer.toString(orders.getDiscount()));
        tv_totalPrice.setText(Integer.toString(orders.getTotalAmount()));
    }

}