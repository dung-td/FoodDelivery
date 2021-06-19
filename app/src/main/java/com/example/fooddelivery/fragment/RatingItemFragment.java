package com.example.fooddelivery.fragment;

import android.app.DialogFragment;
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
import com.example.fooddelivery.model.Comment;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.Orders;
import com.example.fooddelivery.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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

        sendRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidateRating()) {
                    Comment comment = new Comment(
                            new Date().toString(),
                            et_Comment.getText().toString(),
                            Integer.toString(rb.getNumStars())
                    );

                    addProductComment(orderItem.getProduct().getId(), comment);
                }
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

    void addProductComment(String productID, Comment comment) {
        User user = LoginActivity.firebase.getUser();
        Comment data = new Comment(
                comment.getDate(),
                comment.getDetails(),
                comment.getRating(),
                user.getFirst_Name() +  " " + user.getLast_Name()
        );

        FirebaseFirestore root = FirebaseFirestore.getInstance();
        root.collection("Product/"+ productID +"/Comment")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        updateOrderItemComment(documentReference.getId());
                        Toast.makeText(getContext() ,getString(R.string.send_comment_success), Toast.LENGTH_LONG).show();
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
        ArrayList<OrderItem> orders = new ArrayList<>();
        List<Map<String, Object>> listMap = new List<Map<String, Object>>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Map<String, Object>> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(Map<String, Object> stringObjectMap) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Map<String, Object>> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends Map<String, Object>> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Map<String, Object> get(int index) {
                return null;
            }

            @Override
            public Map<String, Object> set(int index, Map<String, Object> element) {
                return null;
            }

            @Override
            public void add(int index, Map<String, Object> element) {

            }

            @Override
            public Map<String, Object> remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Map<String, Object>> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Map<String, Object>> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<Map<String, Object>> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        orders = LoginActivity.firebase.getListOrderedItems(orderItem.getOrder_id());

        for (OrderItem item: orders){
            Map<String, Object> map = new HashMap<>();

            if (item.getProduct().getId().equals(orderItem.getProduct().getId()))
                item.setComment(new Comment(commentId, "", "", "",""));

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

                    }
                });

        //endregion
    }

    boolean checkValidateRating()
    {
        if (rb.getRating() == 0 )
        {
            rb.requestFocus();

            return false;
        }
        if (et_Comment.getText().equals(""))
        {
            et_Comment.requestFocus();
            et_Comment.setError(getString(R.string.rating_null));

            return false;
        }

        return true;
    }
}