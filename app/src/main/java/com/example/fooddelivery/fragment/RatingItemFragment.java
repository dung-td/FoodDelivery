package com.example.fooddelivery.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.model.CallBackData;
import com.example.fooddelivery.model.Comment;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.Orders;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

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
    TextView nullRating;

    OrderItem orderItem;
    ArrayList<OrderItem> listOrderItem = new ArrayList<>();
    boolean ratingSuccess =false;
    Comment returnComment;
    int position;

    static int LIST_RATING_FRAGMENT_CODE = 123456789;

    public RatingItemFragment() {
        // Required empty public constructor
    }

    public RatingItemFragment(OrderItem orderItem, ArrayList<OrderItem> listOrderItem, int position) {
        this.orderItem = orderItem;
        this.listOrderItem = listOrderItem;
        this.position = position;
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
               // getFragmentManager().popBackStack();
                if (ratingSuccess) {
                    Intent intent = new Intent(getContext(), RatingItemFragment.class);
                    intent.putExtra("details", returnComment.getDetails());
                    intent.putExtra("date", returnComment.getDate());
                    intent.putExtra("username", returnComment.getUserName());
                    intent.putExtra("rating", returnComment.getRating());
                    intent.putExtra("position", Integer.toString(position));
                    Log.e("put extra position ", Integer.toString(position));
                    getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                }
                getFragmentManager().popBackStack();
            }
        });

        sendRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidateRating()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Comment comment = new Comment(
                            formatter.format(new Date()).toString(),
                            et_Comment.getText().toString(),
                            Integer.toString(rb.getNumStars())
                    );

                    addProductComment(orderItem.getProduct().getId(), comment);
                }
            }
        });

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                nullRating.setVisibility(View.INVISIBLE);
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
         nullRating = (TextView)getView().findViewById(R.id.tv_rt_pd_null_rating);
         nullRating.setVisibility(View.INVISIBLE);
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

    void addProductComment(String productID, Comment comment) {
        User user = LoginActivity.firebase.getUser();
         returnComment = new Comment(
                comment.getDate(),
                comment.getDetails(),
                comment.getRating(),
                user.getFirst_Name() +  " " + user.getLast_Name()
        );

        FirebaseFirestore root = FirebaseFirestore.getInstance();
        root.collection("Product/"+ productID +"/Comment")
                .add(returnComment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        updateOrderItemComment(documentReference.getId());
                        updateRatingOfProduct();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext() ,getString(R.string.send_feedback_fail), Toast.LENGTH_LONG).show();

                    }
                });
    }

    void updateOrderItemComment(String commentId) {
        //region LOAD_ORDER_ITEMS
//        ArrayList<OrderItem> orders = new ArrayList<>();
        ArrayList<Map<String, Object>> listMap = new ArrayList<>();
//
//        orders = LoginActivity.firebase.getListOrderedItems(orderItem.getOrder_id());
//


        for (OrderItem item: listOrderItem){
            Map<String, Object> map = new HashMap<>();

            if (item.getProduct().getId().equals(orderItem.getProduct().getId()))
                item.setComment(new Comment(commentId, "", "", "", ""));

            map.put("comment", item.getComment().getiD());
            map.put("price", item.getPrice());
            map.put("size", item.getSize());
            map.put("product", item.getProduct().getId());
            map.put("quantity",item.getQuantity());

            listMap.add(map);
        }

        //endregion

        //region UPDATE DATABASE
        FirebaseFirestore root = FirebaseFirestore.getInstance();
        root.collection("User/"+ LoginActivity.firebase.getUserId()+"/Order/")
                .document(orderItem.getOrder_id())
                .update("listItems",listMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LoginActivity.firebase.getListOrdersOfUser();
                        Toast.makeText(getContext(), getString(R.string.send_comment_success), Toast.LENGTH_LONG).show();
                        ratingSuccess = true;
                    }
                });

        //endregion
    }


    void updateRatingOfProduct()
    {
        //UPDATE SALES and RATING on Firebase
        Product ratingProduct = LoginActivity.firebase.getProductById(orderItem.getProduct().getId());
        if (ratingProduct != null){
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);

            float rating = (Integer.parseInt(ratingProduct.getSales())
                    * Float.parseFloat(ratingProduct.getRating()) + Integer.parseInt(returnComment.getRating()))
                    / (Integer.parseInt(ratingProduct.getSales()) + 1);


            ratingProduct.setSales(String.valueOf(Integer.parseInt(ratingProduct.getSales())+1));
            ratingProduct.setRating(String.valueOf(df.format(rating)));

            FirebaseFirestore root = FirebaseFirestore.getInstance();
            root.collection("Product/")
                    .document(ratingProduct.getId())
                    .update("Sales", ratingProduct.getSales(),
                            "Rating", ratingProduct.getRating())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }
    }

    boolean checkValidateRating() {
        if (rb.getRating() == 0.0f )
        {
            Log.e("rating", Float.toString(rb.getRating()));
            nullRating.setVisibility(View.VISIBLE);
            return false;
        }
        if (et_Comment.getText().toString().equals(""))
        {
            Log.e("text", et_Comment.getText().toString());
            et_Comment.requestFocus();
            et_Comment.setError(getString(R.string.rating_null));

            return false;
        }

        return true;
    }
}