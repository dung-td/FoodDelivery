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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RatingProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingProductFragment extends DialogFragment {

    public RatingProductFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RatingProductFragment newInstance(String param1, String param2) {
        RatingProductFragment fragment = new RatingProductFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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
        RatingBar rb = (RatingBar)getView().findViewById(R.id.star_fm_rt_pd);
        EditText et_Comment = (EditText)getView().findViewById(R.id.et_fm_rt_pd_cmt);

        TextView nameProduct = (TextView)getView().findViewById(R.id.tv_fm_rt_pd_productname);
        TextView productCate = (TextView)getView().findViewById(R.id.tv_fm_rt_pd_productcate);
        TextView quantity = (TextView)getView().findViewById(R.id.tv_fm_rt_pd_quantity);
        TextView price = (TextView)getView().findViewById(R.id.tv_fm_rt_pd_price);
        ImageView productPhoto = getView().findViewById(R.id.im_fm_rt_pd_image);
        Button sendRating = (Button)getView().findViewById(R.id.bt_fm_rt_pd_rating);
        ImageButton back = (ImageButton)getView().findViewById(R.id.bt_fm_rt_pd_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                dismiss();
            }
        });
    }
}