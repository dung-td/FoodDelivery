package com.example.fooddelivery.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fooddelivery.adapter.CommentAdapter;
import com.example.fooddelivery.adapter.ImageAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Comment;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.WHITE;

public class ProductActivity extends AppCompatActivity {

    ScrollView scrollViewContent;
    ImageView buttonBack, buttonMore, buttonLove, buttonCart, buttonMerchantInfo;
    TextView textViewImageIndex;
    ViewPager viewPagerImage;

    LinearLayout linearLayoutBack, linearLayoutLove, linearLayoutCart, linearLayoutMore;
    RelativeLayout relativeLayoutToolbar;
    List<Comment> commentList;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initView();
        initProductImageViewPager();
        initRecyclerViewComment();
        changeToolbarColor();
        forwardMerchantActivity();
    }

    private void initView() {
        scrollViewContent = findViewById(R.id.scroll_content);
        buttonBack = findViewById(R.id.ic_back_arrow);
        buttonLove = findViewById(R.id.ic_love);
        buttonCart = findViewById(R.id.ic_cart);
        buttonMore = findViewById(R.id.ic_more);
        buttonMerchantInfo = findViewById(R.id.btn_more_merchant_info);
        textViewImageIndex = findViewById(R.id.tv_image_index);
        viewPagerImage = findViewById(R.id.product_image);
        linearLayoutBack = findViewById(R.id.btn_back_background);
        linearLayoutLove = findViewById(R.id.btn_love_background);
        linearLayoutCart = findViewById(R.id.btn_cart_background);
        linearLayoutMore = findViewById(R.id.btn_more_background);
        relativeLayoutToolbar = findViewById(R.id.toolbar);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.super.onBackPressed();
            }
        });
    }

    public void initProductImageViewPager() {
        int[] imageSlide = new int[]{R.drawable.tra_sen_vang, R.drawable.advertisement, R.drawable.phindi_banner};
        ImageAdapter adapterView = new ImageAdapter(this, imageSlide);
        viewPagerImage.setAdapter(adapterView);
        viewPagerImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                textViewImageIndex.setText((viewPagerImage.getCurrentItem() + 1) + "/" + imageSlide.length);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initRecyclerViewComment() {
        RecyclerView recyclerViewComment = (RecyclerView)findViewById(R.id.recycler_view_comment);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewComment.setLayoutManager(layoutManager);
        getCommentList();
        CommentAdapter commentAdapter = new CommentAdapter(this, commentList);
        recyclerViewComment.setAdapter(commentAdapter);
    }

    public void getCommentList() {
        commentList = new ArrayList<>();
        commentList.add(new Comment(1, "Nguyễn Văn A", "Món ngon, gói đẹp, giá tốt.", R.drawable.male_user_96px, "29/04/2021" , 5));
        commentList.add(new Comment(2, "Nguyễn Văn B", "Món ngon, gói đẹp, giá tốt.", R.drawable.male_user_96px, "20/04/2021" , 4));
        commentList.add(new Comment(3, "Nguyễn Văn C", "Món ngon, gói đẹp, giá tốt.", R.drawable.male_user_96px, "12/04/2021" , 1));
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

    private void forwardMerchantActivity () {
        buttonMerchantInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MerchantActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void turnWhiteToolbar() {
        linearLayoutBack.setBackground(null);
        linearLayoutCart.setBackground(null);
        linearLayoutLove.setBackground(null);
        linearLayoutMore.setBackground(null);
        relativeLayoutToolbar.setBackgroundColor(WHITE);
    }

    private void turnTransparentToolbar() {
        linearLayoutBack.setBackgroundResource(R.drawable.circle_toolbar_button_background);
        linearLayoutCart.setBackgroundResource(R.drawable.circle_toolbar_button_background);
        linearLayoutLove.setBackgroundResource(R.drawable.circle_toolbar_button_background);
        linearLayoutMore.setBackgroundResource(R.drawable.circle_toolbar_button_background);
        relativeLayoutToolbar.setBackground(null);
    }

    @SuppressLint({"NewApi", "UseCompatLoadingForDrawables"})
    private void changeToolbarButtonColorToDarker() {
        Drawable drawable = getDrawable(R.drawable.ic_baseline_arrow_back_24);
        drawable.setTint(Color.parseColor("#00224b"));
        buttonBack.setImageDrawable(drawable);
        drawable = getDrawable(R.drawable.ic_baseline_favorite_border_24);
        drawable.setTint(Color.parseColor("#00224b"));
        buttonLove.setImageDrawable(drawable);
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
        drawable = getDrawable(R.drawable.ic_baseline_favorite_border_24);
        drawable.setTint(Color.parseColor("#FFFFFF"));
        buttonLove.setImageDrawable(drawable);
        drawable = getDrawable(R.drawable.ic_baseline_add_shopping_cart_24);
        drawable.setTint(Color.parseColor("#FFFFFF"));
        buttonCart.setImageDrawable(drawable);
        drawable = getDrawable(R.drawable.ic_baseline_more_horiz_24);
        drawable.setTint(Color.parseColor("#FFFFFF"));
        buttonMore.setImageDrawable(drawable);
    }
}