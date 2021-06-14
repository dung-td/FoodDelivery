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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.main.MainActivity;
import com.example.fooddelivery.adapter.CommentAdapter;
import com.example.fooddelivery.adapter.ImageAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.Comment;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.WHITE;

public class ProductActivity extends AppCompatActivity {

    public static Activity fa;

    ScrollView scrollViewContent;
    ImageView buttonBack, buttonMore, buttonLove, buttonCart, buttonMerchantInfo, imageViewMerchantLogo;
    TextView textViewImageIndex;
    TextView textViewProductNameVn;
    TextView textViewProductPrice;
    TextView textViewMerchantName;
    TextView textViewProductRating;
    static TextView cartBadge;
    TextView textViewProductNameEn;
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
        initButtonMoreMenuPopup();
        addProductToWatchedList();
    }

    private void addProductToCart() {
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                LoginActivity.firebase.cartList.add(product);
                updateCartBadge();
            }
        });
    }

    public static void updateCartBadge() {
        cartBadge.setText(LoginActivity.firebase.cartList.size() + "");
        cartBadge.getBackground().setTint(Color.parseColor("#57BFFF"));
        HomeFragment.updateCartBadge();
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

        //Cart icon
        if (LoginActivity.firebase.cartList.size() > 0) {
            cartBadge.setText(LoginActivity.firebase.cartList.size() + "");
            cartBadge.getBackground().setTint(Color.parseColor("#57BFFF"));
        }

        //Product info
        this.product = (Product) getIntent().getParcelableExtra("Product");
        textViewProductNameVn.setText(product.getName());
        textViewProductNameEn.setText(product.getEn_Name());
        textViewProductPrice.setText(product.getPrice().get(0));
        textViewProductRating.setText(product.getRating());
        productSize = new ArrayList<String>();
        productSize = product.getProductSize();

        //Merchant info
        textViewMerchantName.setText(product.getMerchant().getName() + " - " + product.getMerchant().getAddress());
        if (product.getMerchant().getImage().size() > 0) { //Merchant logo
            imageViewMerchantLogo.setBackground(null);
            Glide.with(getApplicationContext()).load(product.getMerchant().getImage().get(0)).into(imageViewMerchantLogo);
        }

        //Back button
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

        //Init spinner
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
        commentList = product.getCommentList();
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

    private void initButtonMoreMenuPopup() {
        LayoutInflater layoutInflater= (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.more_popup_layout, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,330, LinearLayout.LayoutParams.WRAP_CONTENT);

        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow.isShowing())
                    popupWindow.dismiss();
                else
                    popupWindow.showAsDropDown(buttonMore, 105, 30);
            }
        });
        TextView buttonHome = popupView.findViewById(R.id.more_popup_item_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        TextView buttonMe = popupView.findViewById(R.id.more_popup_item_me);
        buttonMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addProductToWatchedList () {
        if (!LoginActivity.firebase.watchedList.contains(product.getId())) {
            LoginActivity.firebase.watchedList.add(product.getId());
            LoginActivity.firebase.addProductToWatched(product.getId());
        }
    }
}