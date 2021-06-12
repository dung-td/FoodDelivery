package com.example.fooddelivery.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.ImageAdapter;
import com.example.fooddelivery.adapter.MyAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Merchant;
import com.example.fooddelivery.model.MyViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static android.graphics.Color.WHITE;


public class MerchantActivity extends AppCompatActivity {

    ScrollView scrollViewContent;
    ImageView imageViewLogo, buttonBack, buttonMore, buttonCart;
    TextView textViewBannerIndex, textViewMerchantName, textViewRating, textViewTime, textViewDistance;
    TabLayout tabLayout;
    MyViewPager viewPager;
    ViewPager viewPagerBanner;
    LinearLayout linearLayoutBack, linearLayoutLove, linearLayoutCart, linearLayoutMore;
    RelativeLayout relativeLayoutToolbar;
    Merchant merchant;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        initView();
        initTabLayoutAndViewPager();
        changeToolbarColor();
        changeToolbarButtonColorToLighter();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        scrollViewContent = findViewById(R.id.scroll_content);
        buttonBack = findViewById(R.id.ic_back_arrow);
        buttonCart = findViewById(R.id.ic_cart);
        buttonMore = findViewById(R.id.ic_more);
        imageViewLogo = findViewById(R.id.img_logo);
        textViewBannerIndex = findViewById(R.id.tv_banner_index);
        textViewMerchantName = findViewById(R.id.tv_merchant_name);
        textViewRating = findViewById(R.id.tv_rating);
        textViewTime = findViewById(R.id.tv_time);
        textViewDistance = findViewById(R.id.tv_distance);
        tabLayout = findViewById(R.id.tab_button);
        viewPager = findViewById(R.id.info_viewpager);
        viewPagerBanner = findViewById(R.id.merchant_banner);
        linearLayoutBack = findViewById(R.id.btn_back_background);
        linearLayoutLove = findViewById(R.id.btn_love_background);
        linearLayoutCart = findViewById(R.id.btn_cart_background);
        linearLayoutMore = findViewById(R.id.btn_more_background);
        relativeLayoutToolbar = findViewById(R.id.toolbar);
        this.merchant = (Merchant) LoginActivity.firebase.productList.get(getIntent().
                getIntExtra("ClickedProductIndex", 0)).getMerchant();
        textViewMerchantName.setText(merchant.getName() + " - " + merchant.getAddress());

        tabLayout.addTab(tabLayout.newTab().setText("MENU"));
        tabLayout.addTab(tabLayout.newTab().setText("BÌNH LUẬN"));
        tabLayout.addTab(tabLayout.newTab().setText("THÔNG TIN"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToolbarButtonColorToLighter();
            }
        });
    }

    private void initTabLayoutAndViewPager() {
//        3 View Buttons ViewPager
        MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        Banner ViewPager
        ImageAdapter adapterView = new ImageAdapter(this, merchant.getImage());
        viewPagerBanner.setAdapter(adapterView);
        viewPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                textViewBannerIndex.setText((viewPagerBanner.getCurrentItem() + 1) + "/" + merchant.getImage().size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changeToolbarColor() {
        scrollViewContent.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 0) {
                    turnWhiteToolbar();
                    changeToolbarButtonColorToDarker();
                } else if (scrollViewContent.getScrollY() == scrollY) {
                    turnTransparentToolbar();
                    changeToolbarButtonColorToLighter();
                }
            }
        });
    }

    private void turnWhiteToolbar() {
        linearLayoutBack.setBackground(null);
        linearLayoutCart.setBackground(null);
        linearLayoutMore.setBackground(null);
        relativeLayoutToolbar.setBackgroundColor(WHITE);
    }

    private void turnTransparentToolbar() {
        linearLayoutBack.setBackgroundResource(R.drawable.circle_toolbar_button_background);
        linearLayoutCart.setBackgroundResource(R.drawable.circle_toolbar_button_background);
        linearLayoutMore.setBackgroundResource(R.drawable.circle_toolbar_button_background);
        relativeLayoutToolbar.setBackground(null);
    }

    @SuppressLint({"NewApi", "UseCompatLoadingForDrawables"})
    private void changeToolbarButtonColorToDarker() {
        Drawable drawable = getDrawable(R.drawable.ic_baseline_arrow_back_24);
        drawable.setTint(Color.parseColor("#00224b"));
        buttonBack.setImageDrawable(drawable);
        drawable = getDrawable(R.drawable.ic_baseline_add_shopping_cart_24);
        drawable.setTint(Color.parseColor("#00224b"));
        buttonCart.setImageDrawable(drawable);
        drawable = getDrawable(R.drawable.ic_baseline_more_horiz_24);
        drawable.setTint(Color.parseColor("#00224b"));
        buttonMore.setImageDrawable(drawable);
    }

    @SuppressLint({"NewApi", "UseCompatLoadingForDrawables"})
    private void changeToolbarButtonColorToLighter() {
        Drawable drawable = getDrawable(R.drawable.ic_baseline_arrow_back_24);
        drawable.setTint(Color.parseColor("#FFFFFF"));
        buttonBack.setImageDrawable(drawable);
        drawable = getDrawable(R.drawable.ic_baseline_add_shopping_cart_24);
        drawable.setTint(Color.parseColor("#FFFFFF"));
        buttonCart.setImageDrawable(drawable);
        drawable = getDrawable(R.drawable.ic_baseline_more_horiz_24);
        drawable.setTint(Color.parseColor("#FFFFFF"));
        buttonMore.setImageDrawable(drawable);
    }
}