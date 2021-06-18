package com.example.fooddelivery.fragment;

import android.app.DialogFragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.model.OrderItem;
import com.squareup.picasso.Picasso;

public class RatingItemFragment extends Fragment {

    RatingBar rb;
    EditText et_Comment;

    TextView nameProduct;
    TextView productCate;
    TextView quantity;
    TextView price;
    ImageView productPhoto;
    Button sendRating;
    ImageButton back ;

    OrderItem orderItem;

    public RatingItemFragment() {
        // Required empty public constructor
    }

    public RatingItemFragment(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_rating_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        setData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getActivity().onBackPressed();
                getFragmentManager().popBackStack();
            }
        });
    }

    void initView() {
         rb = (RatingBar)getView().findViewById(R.id.star_fm_rt_pd);
         et_Comment = (EditText)getView().findViewById(R.id.et_fm_rt_pd_cmt);

         nameProduct = (TextView)getView().findViewById(R.id.tv_fm_rt_pd_productname);
         productCate = (TextView)getView().findViewById(R.id.tv_fm_rt_pd_productcate);
         quantity = (TextView)getView().findViewById(R.id.tv_fm_rt_pd_quantity);
         price = (TextView)getView().findViewById(R.id.tv_fm_rt_pd_price);
         productPhoto = getView().findViewById(R.id.im_fm_rt_pd_image);
         sendRating = (Button)getView().findViewById(R.id.bt_fm_rt_pd_rating);
         back = (ImageButton)getView().findViewById(R.id.bt_fm_rt_pd_back);
    }

    void setData(){
        setNameItem();
        productCate.setText(orderItem.getSize());
        quantity.setText(Integer.toString(orderItem.getQuantity()));
        price.setText(Integer.toString(orderItem.getPrice()));

        Picasso.get().load(orderItem.getProduct().getImage().get(0))
                .into(productPhoto);
    }

    void setNameItem() {
        if (LoginActivity.language.equals("vi"))
            nameProduct.setText(String.format(orderItem.getProduct().getName()));
        else
            nameProduct.setText(String.format(orderItem.getProduct().getEn_Name()));
    }
}