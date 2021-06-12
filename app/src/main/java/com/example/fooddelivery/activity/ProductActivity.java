package com.example.fooddelivery.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.CommentAdapter;
import com.example.fooddelivery.adapter.ImageAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapter.ItemOnMainAdapter;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.Comment;
import com.example.fooddelivery.model.Merchant;
import com.example.fooddelivery.model.ModifyFirebase;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.WHITE;

public class ProductActivity extends AppCompatActivity {

    public static Activity fa;

    ScrollView scrollViewContent;
    ImageView buttonBack, buttonMore, buttonLove, buttonCart, buttonMerchantInfo, imageViewMerchantLogo;
    TextView textViewImageIndex, textViewProductNameVn, textViewProductPrice, textViewMerchantName, textViewProductRating,
                cartBadge, textViewProductNameEn;
    ViewPager viewPagerImage;
    ImageButton buttonAddToCart;
    Spinner spinnerProductSize;

    LinearLayout linearLayoutBack, linearLayoutLove, linearLayoutMore;
    FrameLayout frameLayoutCart;
    RelativeLayout relativeLayoutToolbar;
    List<Comment> commentList;
    ArrayList<String> productSize;
    Product product;
    boolean isFavourite = false;
    boolean favouriteStateChange = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        fa = this;

        initView();
        changeToolbarButtonColorToLighter();
        changeLoveIcon();
        initProductImageViewPager();
        initRecyclerViewComment();
        changeToolbarColor();
        forwardMerchantActivity();
        addProductToFavourite();
        addProductToCart();
    }

    private void addProductToCart() {
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int quantity = 0;
                if (!cartBadge.getText().toString().equals("")) {
                    quantity = Integer.parseInt(cartBadge.getText().toString()) + 1;
                    cartBadge.setText(quantity + "");
                }
                else {
                    cartBadge.setText("1");
                }
                cartBadge.getBackground().setTint(Color.parseColor("#57BFFF"));
            }
        });
    }

    private void addProductToFavourite() {
        buttonLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourite) {
                    isFavourite = false;
                    buttonLove.setImageResource(R.drawable.ic_baseline_favourite_white_border_24);
                    LoginActivity.firebase.favouriteProductList.remove(product.getId());
                    LoginActivity.firebase.removeProductFromFavourite(getApplicationContext(), product.getId());
                }
                else {
                    isFavourite = true;
                    buttonLove.setImageResource(R.drawable.ic_baseline_favorite_24);
                    LoginActivity.firebase.favouriteProductList.add(product.getId());
                    LoginActivity.firebase.addProductToFavourite(getApplicationContext(), product.getId());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        scrollViewContent = findViewById(R.id.scroll_content);
        buttonBack = findViewById(R.id.ic_back_arrow);
        buttonLove = findViewById(R.id.ic_love);
        buttonCart = findViewById(R.id.ic_cart);
        buttonMore = findViewById(R.id.ic_more);
        buttonAddToCart = findViewById(R.id.btn_add_item);
        buttonMerchantInfo = findViewById(R.id.btn_more_merchant_info);
        textViewImageIndex = findViewById(R.id.tv_image_index);
        textViewProductNameVn = findViewById(R.id.tv_product_name_vn);
        textViewProductPrice = findViewById(R.id.tv_product_price);
        textViewMerchantName = findViewById(R.id.tv_merchant_name);
        textViewProductRating = findViewById(R.id.tv_rating);
        textViewProductNameEn = findViewById(R.id.tv_product_name_en);
        cartBadge = findViewById(R.id.cart_badge);
        viewPagerImage = findViewById(R.id.product_image);
        linearLayoutBack = findViewById(R.id.btn_back_background);
        linearLayoutLove = findViewById(R.id.btn_love_background);
        frameLayoutCart = findViewById(R.id.btn_cart_background);
        linearLayoutMore = findViewById(R.id.btn_more_background);
        relativeLayoutToolbar = findViewById(R.id.toolbar);
        imageViewMerchantLogo = findViewById(R.id.img_logo);
        spinnerProductSize = findViewById(R.id.spinner_size);

        this.product = (Product) LoginActivity.firebase.productList.get(getIntent().
                getIntExtra("ClickedProductIndex", 0));
        textViewProductNameVn.setText(product.getName());
        textViewProductNameEn.setText(product.getEn_Name());
        textViewProductPrice.setText(product.getPrice().get(0));
        textViewProductRating.setText(product.getRating());
        productSize = new ArrayList<String>();
        productSize = product.getProductSize();
        textViewMerchantName.setText(product.getMerchant().getName() + " - " + product.getMerchant().getAddress());
        imageViewMerchantLogo.setImageURI(product.getMerchant().getImage().get(0));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (isFavourite != favouriteStateChange) {
                    HomeFragment.itemOnMainAdapter.notifyDataSetChanged();
                }
                ProductActivity.super.onBackPressed();
            }
        });
        initSpinnerProductSize();
    }

    @Override
    public void onBackPressed() {
        if (isFavourite != favouriteStateChange) {
            HomeFragment.itemOnMainAdapter.notifyDataSetChanged();
        }
        super.onBackPressed();
    }

    private void changeLoveIcon() {
        if (getIntent().getExtras().getBoolean("IsFavourite")) {
            isFavourite = true;
            favouriteStateChange = true;
            buttonLove.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
    }

    private void initSpinnerProductSize() {
        ArrayAdapter<String> staticAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, productSize);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductSize.setAdapter(staticAdapter);

        spinnerProductSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewProductPrice.setText(product.getPrice().get(position) + " d");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initProductImageViewPager() {
        ArrayList<Uri> imageSlide = new ArrayList<Uri>();
        imageSlide = product.getImage();
        ImageAdapter adapterView = new ImageAdapter(this, imageSlide);
        viewPagerImage.setAdapter(adapterView);
        ArrayList<Uri> finalImageSlide = imageSlide;
        viewPagerImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                textViewImageIndex.setText((viewPagerImage.getCurrentItem() + 1) + "/" + finalImageSlide.size());
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
                intent.putExtra("ClickProductIndex", getIntent().getIntExtra("ClickedProductIndex", 0));
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void turnWhiteToolbar() {
        linearLayoutBack.setBackground(null);
        frameLayoutCart.setBackground(null);
        linearLayoutLove.setBackground(null);
        linearLayoutMore.setBackground(null);
        relativeLayoutToolbar.setBackgroundColor(WHITE);
    }

    private void turnTransparentToolbar() {
        linearLayoutBack.setBackgroundResource(R.drawable.circle_toolbar_button_background);
        frameLayoutCart.setBackgroundResource(R.drawable.circle_toolbar_button_background);
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
        Drawable drawable = getDrawable(R.drawable.ic_baseline_favourite_white_border_24);
        buttonLove.setImageDrawable(drawable);
        drawable = getDrawable(R.drawable.ic_baseline_arrow_back_24);
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