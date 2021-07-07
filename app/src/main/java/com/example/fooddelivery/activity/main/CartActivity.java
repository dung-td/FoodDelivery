package com.example.fooddelivery.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.activity.me.PersonalInfoActivity;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.ChosenItemAdapter;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.ChosenItem;
import com.example.fooddelivery.model.DirectionFinder;
import com.example.fooddelivery.model.DirectionFinderListener;
import com.example.fooddelivery.model.Merchant;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.Route;
import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    ImageButton buttonBack, buttonChangeOrderTime, buttonChooseVoucher;
    Button buttonProceedOrder;
    static ImageButton buttonRemoveVoucher;
    static Button voucherCode;
    static TextView textViewNamePhone;
    static TextView textViewAddress;
    TextView textViewChangeAddress;
    public static TextView textViewDistance;
    static TextView textViewApplyVoucherFailed;
    static TextView textViewOrderTime;
    static TextView textViewSumPrice;
    static TextView textViewShippingCost;
    static TextView textViewTotalCost;
    static TextView textViewVoucherDiscount;
    TextView textViewNoData;
    ArrayList<ChosenItem> itemList;
    public static String totalPrice, deliveryTime, voucherId = "null", voucherMinSumPrice = "0";
    public static ChosenItemAdapter chosenItemAdapter;
    int LAUNCH_CHANGE_TIME_ACTIVITY = 1;
    int LAUNCH_CHOOSE_VOUCHER_ACTIVITY = 2;
    int LAUNCH_FINISH_ORDER_ACTIVITY = 3;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        initView();
        getMerchantDistance();
        loadLanguage();
        setUserInformation();
        initRecyclerViewItemOnCart();
        proceedOrder();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        buttonProceedOrder = findViewById(R.id.btn_proceed_order);
        buttonChangeOrderTime = findViewById(R.id.btn_change_order_time);
        buttonChooseVoucher = findViewById(R.id.btn_choose_voucher);
        voucherCode = findViewById(R.id.tv_voucher_code);
        buttonRemoveVoucher = findViewById(R.id.btn_remove_voucher);
        textViewAddress = findViewById(R.id.tv_user_address);
        textViewChangeAddress = findViewById(R.id.btn_change_address);
        textViewNamePhone = findViewById(R.id.tv_user_name_phone);
        textViewOrderTime = findViewById(R.id.tv_order_time);
        textViewSumPrice = findViewById(R.id.tv_price_sum);
        textViewShippingCost = findViewById(R.id.tv_shipping_cost);
        textViewTotalCost = findViewById(R.id.tv_total_cost);
        textViewNoData = findViewById(R.id.tv_no_data);
        textViewDistance = findViewById(R.id.tv_distance);
        textViewApplyVoucherFailed = findViewById(R.id.tv_apply_voucher_failed);
        textViewVoucherDiscount = findViewById(R.id.tv_voucher_discount);

        String nextOneHour = checkDigit(Calendar.getInstance().getTime().getHours() + 1) + ":"
                + checkDigit(Calendar.getInstance().getTime().getMinutes());
        textViewOrderTime.setText(getString(R.string.deliver_now) + " - " + nextOneHour + " - " + getString(R.string.today));
        deliveryTime = nextOneHour;

        textViewChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, PersonalInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.super.onBackPressed();
            }
        });

        buttonChangeOrderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ChangeOrderTimeActivity.class);
                startActivityForResult(intent, LAUNCH_CHANGE_TIME_ACTIVITY);
            }
        });

        buttonChooseVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, AvailableVoucherActivity.class);
                intent.putExtra("Type", "Order");
                startActivityForResult(intent, LAUNCH_CHOOSE_VOUCHER_ACTIVITY);
            }
        });

        buttonRemoveVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelVoucher();
            }
        });
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public static void cancelVoucher() {
        textViewVoucherDiscount.setText("0");
        textViewApplyVoucherFailed.setVisibility(View.GONE);
        voucherCode.setVisibility(View.INVISIBLE);
        buttonRemoveVoucher.setVisibility(View.INVISIBLE);
        updateTotalPriceAndCost();
    }

    public void initRecyclerViewItemOnCart() {
        RecyclerView recyclerViewItemOnCart = (RecyclerView)findViewById(R.id.recycler_view_item_on_cart);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewItemOnCart.setLayoutManager(layoutManager);
        getChosenItemsList();
        if (WelcomeActivity.firebase.cartList.size() > 0) {
            textViewNoData.setVisibility(View.INVISIBLE);
        }
        totalPrice = "0";
        chosenItemAdapter = new ChosenItemAdapter(this, WelcomeActivity.firebase.cartList);
        recyclerViewItemOnCart.setAdapter(chosenItemAdapter);
    }

    private void getChosenItemsList() {
        if (HomeFragment.isCartFirstClick) {
            HomeFragment.isCartFirstClick = false;
            itemList = new ArrayList<>();
            ArrayList<String> itemId = new ArrayList<>();
            for (Product p : WelcomeActivity.firebase.productList) {
                itemId.add(p.getId());
            }
            for (ChosenItem item : WelcomeActivity.firebase.cartList) {
                WelcomeActivity.firebase.cartList.get(WelcomeActivity.firebase.cartList.indexOf(item))
                        .setProduct(WelcomeActivity.firebase.productList.get(itemId.indexOf(item.getProduct().getId())));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public static void updateTotalPriceAndCost() {
        textViewSumPrice.setText(totalPrice);
        textViewTotalCost.setText((Integer.parseInt(totalPrice) + Integer.parseInt(textViewShippingCost.getText().toString())
            - Integer.parseInt(textViewVoucherDiscount.getText().toString())) + " d");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_CHANGE_TIME_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                textViewOrderTime.setText(getString(R.string.deliver_now) + " - " + result + " - " + getString(R.string.today));
                deliveryTime = result;
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        if (requestCode == LAUNCH_CHOOSE_VOUCHER_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                if (checkVoucherUtilization(data.getStringExtra("minSumPrice"))) {
                    voucherMinSumPrice = data.getStringExtra("minSumPrice");
                    voucherId = data.getStringExtra("voucherId");
                    textViewApplyVoucherFailed.setVisibility(View.GONE);
                    int appliedDiscount = (Integer.parseInt(data.getStringExtra("percentDiscount")) / 100)
                            * Integer.parseInt(textViewSumPrice.getText().toString());
                    if (appliedDiscount > Integer.parseInt(data.getStringExtra("maxDiscount"))) {
                        appliedDiscount = Integer.parseInt(data.getStringExtra("maxDiscount"));
                    }
                    textViewVoucherDiscount.setText(String.valueOf(appliedDiscount));
                    updateTotalPriceAndCost();

                    voucherCode.setText(data.getStringExtra("voucherCode"));
                    voucherCode.setVisibility(View.VISIBLE);
                    buttonRemoveVoucher.setVisibility(View.VISIBLE);
                }
                else {
                    textViewApplyVoucherFailed.setVisibility(View.VISIBLE);
                    voucherCode.setVisibility(View.INVISIBLE);
                    buttonRemoveVoucher.setVisibility(View.INVISIBLE);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        if (requestCode == LAUNCH_FINISH_ORDER_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    @SuppressLint("SetTextI18n")
    public static void setUserInformation() {
        String name = WelcomeActivity.firebase.getUser().getLast_Name() + " " + WelcomeActivity.firebase.getUser().getFirst_Name();
        String phone = WelcomeActivity.firebase.getUser().getPhone_Number();
        textViewNamePhone.setText(name + " - " + phone);
        textViewAddress.setText(WelcomeActivity.firebase.getUser().getAddress().getAddressLine(0));
    }

    void loadLanguage() {
        String langPref = "lang_code";
        SharedPreferences prefs = getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");

        Log.e("language", language);

        Locale locale = new Locale(language);
        locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static boolean checkVoucherUtilization(String minSumPrice) {
        return Integer.parseInt(textViewSumPrice.getText().toString()) >= Integer.parseInt(minSumPrice);
    }

    private void proceedOrder() {
        buttonProceedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WelcomeActivity.firebase.cartList.size() > 0) {
                    if (!checkDifferentMerchant()) {
                        Intent intent = new Intent(CartActivity.this, FinishOrderActivity.class);
                        startActivityForResult(intent, LAUNCH_FINISH_ORDER_ACTIVITY);
                    }
                    else {
                        new AlertDialog.Builder(CartActivity.this)
                                .setTitle(getString(R.string.title_invalid_order))
                                .setMessage(getString(R.string.title_different_merchant))
                                .setPositiveButton(android.R.string.ok, null)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                }
            }
        });
    }

    private boolean checkDifferentMerchant() {
        String previousMerchant = WelcomeActivity.firebase.cartList.get(0).getProduct().getMerchant().getId();
        for (ChosenItem chosenItem : WelcomeActivity.firebase.cartList) {
            if (!chosenItem.getProduct().getMerchant().getId().equals(previousMerchant)) {
                return true;
            }
            previousMerchant = chosenItem.getProduct().getMerchant().getId();
        }
        return false;
    }

    public static void calculateShippingCost() {
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.CEILING);

        int shippingCost = 0;
        String distance = textViewDistance.getText().toString().substring(0, textViewDistance.getText().toString().indexOf(" "));
        shippingCost = (Integer.parseInt(df.format(Float.parseFloat(distance))) / 3) * 5000;

        textViewShippingCost.setText(String.valueOf(shippingCost));
    }

    public static void getMerchantDistance() {
        if (WelcomeActivity.firebase.cartList.size() > 0) {
            double latitude = 0.0;
            double longitude = 0.0;
            for (Merchant merchant : WelcomeActivity.firebase.merchantList) {

                if (merchant.getRoutes().size() == 0) {

                    String firstItemMerchantId = WelcomeActivity.firebase.getProductById(
                            WelcomeActivity.firebase.cartList.get(0).getProduct().getId()).getMerchant().getId();
                    if (firstItemMerchantId.equals(merchant.getId())) {
                        latitude = merchant.getAddress().getLatitude();
                        longitude = merchant.getAddress().getLongitude();

                        try {
                            LatLng fromLatLng = new LatLng(latitude, longitude);
                            LatLng toLatLng = new LatLng(WelcomeActivity.firebase.getUser().getAddress().getLatitude(), WelcomeActivity.firebase.getUser().getAddress().getLongitude());
                            new DirectionFinder(new DirectionFinderListener() {
                                @Override
                                public void onDirectionFinderStart() {
                                }

                                @Override
                                public void onDirectionFinderSuccess(List<Route> route) {
                                    merchant.getRoutes().addAll(route);
                                    textViewDistance.setText(merchant.getRoutes().get(0).distance.text);
                                    calculateShippingCost();
                                    updateTotalPriceAndCost();
                                    textViewDistance.setVisibility(View.VISIBLE);
                                }
                            }, fromLatLng, toLatLng).execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                } else {
                    String firstItemMerchantId = WelcomeActivity.firebase.getProductById(
                            WelcomeActivity.firebase.cartList.get(0).getProduct().getId()).getMerchant().getId();
                    if (firstItemMerchantId.equals(merchant.getId())) {
                        textViewDistance.setText(merchant.getRoutes().get(0).distance.text);
                        calculateShippingCost();
                        updateTotalPriceAndCost();
                        textViewDistance.setVisibility(View.VISIBLE);
                    }
                }

            }
        }
        else {
            textViewDistance.setVisibility(View.INVISIBLE);
        }
    }
}
