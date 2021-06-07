package com.example.fooddelivery.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.adapter.ChosenItemAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.ChosenItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    ImageButton buttonBack, buttonChangeOrderTime;
    TextView textViewNamePhone, textViewAddress, textViewChangeAddress, textViewOrderTime, textViewMerchant, textViewSumPrice,
            textViewShippingCost, textViewTotalCost;
    List<ChosenItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        initView();
        initRecyclerViewItemOnCart();
        getCosts();
    }

    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        buttonChangeOrderTime = findViewById(R.id.btn_change_order_time);
        textViewAddress = findViewById(R.id.tv_address);
        textViewChangeAddress = findViewById(R.id.btn_change_address);
        textViewNamePhone = findViewById(R.id.tv_user_name_phone);
        textViewOrderTime = findViewById(R.id.tv_order_time);
        textViewMerchant = findViewById(R.id.tv_merchant_name_cart);
        textViewSumPrice = findViewById(R.id.tv_price_sum);
        textViewShippingCost = findViewById(R.id.tv_shipping_cost);
        textViewTotalCost = findViewById(R.id.tv_total_cost);
    }

    public void initRecyclerViewItemOnCart() {
        RecyclerView recyclerViewItemOnCart = (RecyclerView)findViewById(R.id.recycler_view_item_on_cart);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewItemOnCart.setLayoutManager(layoutManager);
        getChosenItemsList();
        ChosenItemAdapter chosenItemAdapter = new ChosenItemAdapter(this, itemList);
        recyclerViewItemOnCart.setAdapter(chosenItemAdapter);
    }

    private void getChosenItemsList() {
        itemList = new ArrayList<>();
        itemList.add(new ChosenItem(1, R.drawable.green_tea_freeze, "Freeze Trà Xanh", "L", 49000, 2, "Không đá", ""));
        itemList.add(new ChosenItem(1, R.drawable.trasenvang, "Trà Sen Vàng", "M", 49000, 1, "Không đá", ""));
    }

    @SuppressLint("SetTextI18n")
    private void getCosts () {
        int sum = 0;
        for (int i = 0; i < itemList.size(); i++) {
            sum += itemList.get(i).getItem_price() * itemList.get(i).getQuantity();
        }
        textViewSumPrice.setText(sum + " đ");
        textViewTotalCost.setText((sum + 15000) + " đ");
    }
}
