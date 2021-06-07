package com.example.fooddelivery.adapter;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ItemOnSectionFragment extends Fragment {

    private static List<Product> productList;
    private boolean isEndLoop;

    public ItemOnSectionFragment() {
        productList = new ArrayList<>();
        isEndLoop = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getData("");
    }

    public void getData(String keyword) {
        FirebaseFirestore root1 = FirebaseFirestore.getInstance();
        root1.collection("Merchant/EOPPrOWpbfp2XCcjCQkT/Drinks")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            Product product = new Product();
                            product.setName((String) document.get("Name"));
                            product.setPrice(document.get("Price") + "");
                            product.setRating(document.get("Rating") + "");
                            getImage(product);
                        }
                        isEndLoop = true;
                    }
                });
    }

    private void initRecyclerViewAndAdapter () {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_item);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ProductOnSectionAdapter productAdapter = new ProductOnSectionAdapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);
    }

    private void getImage(Product p) {
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = reference.child("gtf.png");
        fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                p.setImage(task.getResult());
                productList.add(p);
                while (isEndLoop) {
                    initRecyclerViewAndAdapter();
                    isEndLoop = false;
                    return;
                }
            }
        });
        return;
    }

    private void setImage(Uri uri, Product p) {
        p.setImage(uri);
        productList.add(p);
    }
}
